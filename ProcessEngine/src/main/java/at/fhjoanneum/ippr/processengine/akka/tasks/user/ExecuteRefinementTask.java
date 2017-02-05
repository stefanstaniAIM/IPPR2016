package at.fhjoanneum.ippr.processengine.akka.tasks.user;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

import akka.pattern.PatternsCS;
import at.fhjoanneum.ippr.commons.dto.processengine.StateDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.stateobject.BusinessObjectDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.stateobject.BusinessObjectFieldInstanceDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.stateobject.BusinessObjectInstanceDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.stateobject.StateObjectChangeDTO;
import at.fhjoanneum.ippr.persistence.objects.engine.state.SubjectState;
import at.fhjoanneum.ippr.processengine.akka.config.Global;
import at.fhjoanneum.ippr.processengine.akka.messages.process.refinement.ExecuteRefinementMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.refinement.ExecuteRefinementMessage.Request;
import at.fhjoanneum.ippr.processengine.akka.messages.process.workflow.StateObjectChangeMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.workflow.StateObjectMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.workflow.StateObjectMessage.Response;
import at.fhjoanneum.ippr.processengine.akka.tasks.AbstractTask;
import at.fhjoanneum.ippr.processengine.refinements.core.BusinessObject;
import at.fhjoanneum.ippr.processengine.refinements.core.BusinessObjectField;
import at.fhjoanneum.ippr.processengine.refinements.core.Refinement;
import at.fhjoanneum.ippr.processengine.repositories.SubjectStateRepository;

@Component("User.ExecuteRefinement")
@Scope("prototype")
public class ExecuteRefinementTask extends AbstractTask<ExecuteRefinementMessage.Request> {

  private final static Logger LOG = LoggerFactory.getLogger(ExecuteRefinementTask.class);

  private final static String REFINEMENT_PACKAGE =
      "at.fhjoanneum.ippr.processengine.refinements.executions.";

  @Autowired
  private SubjectStateRepository subjectStateRepository;

  @Override
  public boolean canHandle(final Object obj) {
    return obj instanceof ExecuteRefinementMessage.Request;
  }

  @Override
  public void execute(final Request message) throws Exception {
    final SubjectState subjectState = subjectStateRepository.findOne(message.getSubjectStateId());

    final Long userId = subjectState.getSubject().getUser();
    final Long piId = subjectState.getProcessInstance().getPiId();

    final StateObjectMessage.Response stateObject = (Response) PatternsCS
        .ask(getContext().parent(), getStateObjectMessage(piId, userId), Global.TIMEOUT)
        .toCompletableFuture().get();

    final Refinement refinement =
        loadRefinement(subjectState.getCurrentState().getRefinementState().getRefinementClass());
    final Map<String, BusinessObject> businessObjects = refinement
        .execute(getBusinessObjects(stateObject.getStateObjectDTO().getBusinessObjects()));
    final Long nextStateId =
        refinement.getNextStateId(getNextStates(stateObject.getStateObjectDTO().getNextStates()));

    Objects.requireNonNull(businessObjects);
    Objects.requireNonNull(nextStateId);

    sendChangeStateObjectMessage(piId, userId, businessObjects, nextStateId);
  }

  private StateObjectMessage.Request getStateObjectMessage(final Long piId, final Long userId) {
    return new StateObjectMessage.Request(piId, userId, true);
  }

  private Map<String, BusinessObject> getBusinessObjects(
      final List<BusinessObjectDTO> businessObjectsDTO) {
    if (CollectionUtils.isEmpty(businessObjectsDTO)) {
      return Maps.newHashMap();
    }
    return businessObjectsDTO.stream().map(this::getBusinessObject)
        .collect(Collectors.toMap(BusinessObject::getName, bo -> bo));
  }

  private BusinessObject getBusinessObject(final BusinessObjectDTO businessObjectDTO) {
    final Long bomId = businessObjectDTO.getBomId();
    final String name = businessObjectDTO.getName();
    final Map<String, BusinessObject> children = Maps.newHashMap();

    final Map<String, BusinessObjectField> fields = businessObjectDTO.getFields()
        .stream().map(field -> new BusinessObjectField(field.getBofmId(), field.getName(),
            field.getType(), field.getValue()))
        .collect(Collectors.toMap(BusinessObjectField::getFieldName, f -> f));

    if (!CollectionUtils.isEmpty(businessObjectDTO.getChildren())) {
      for (final BusinessObjectDTO childDTO : businessObjectDTO.getChildren()) {
        final BusinessObject child = getBusinessObject(childDTO);
        children.put(child.getName(), child);
      }
    }
    return new BusinessObject(bomId, name, fields, children);
  }

  private Map<String, Long> getNextStates(final List<StateDTO> states) {
    return states.stream().collect(Collectors.toMap(StateDTO::getName, StateDTO::getNextStateId));
  }

  private Refinement loadRefinement(final String clazzName) {
    try {
      final Refinement refinement = this.getClass().getClassLoader()
          .loadClass(REFINEMENT_PACKAGE + clazzName).asSubclass(Refinement.class).newInstance();
      LOG.debug("Sucessfully loaded refinement: {}", refinement.getClass().getName());
      return refinement;
    } catch (final Exception e) {
      LOG.error(e.getMessage());
      throw new IllegalStateException("Could not load refinement for clazz: " + clazzName);
    }
  }

  private StateObjectChangeDTO createStateObjectChangeDTO(
      final List<BusinessObject> businessObjects, final Long nextStateId) {
    final List<BusinessObjectInstanceDTO> businessObjectsDTO = businessObjects.stream()
        .map(this::getBusinessObjectInstanceDTO).collect(Collectors.toList());

    return new StateObjectChangeDTO(nextStateId, businessObjectsDTO, Lists.newArrayList());
  }

  private BusinessObjectInstanceDTO getBusinessObjectInstanceDTO(
      final BusinessObject businessObject) {
    final Long bomId = businessObject.getBomId();

    final List<BusinessObjectFieldInstanceDTO> fields = businessObject.getFields().values().stream()
        .map(field -> new BusinessObjectFieldInstanceDTO(field.getBofmId(), field.getValue()))
        .collect(Collectors.toList());

    final List<BusinessObjectInstanceDTO> children = Lists.newArrayList();
    if (CollectionUtils.isEmpty(businessObject.getChildren().values())) {
      for (final BusinessObject child : businessObject.getChildren().values()) {
        children.add(getBusinessObjectInstanceDTO(child));
      }
    }
    return new BusinessObjectInstanceDTO(bomId, fields, children);
  }

  private void sendChangeStateObjectMessage(final Long piId, final Long userId,
      final Map<String, BusinessObject> businessObjects, final Long nextStateId) {
    final StateObjectChangeDTO stateObjectChangeDTO =
        createStateObjectChangeDTO(Lists.newArrayList(businessObjects.values()), nextStateId);
    final StateObjectChangeMessage.Request request =
        new StateObjectChangeMessage.Request(piId, userId, stateObjectChangeDTO);

    PatternsCS.ask(getContext().parent(), request, Global.TIMEOUT).toCompletableFuture()
        .whenComplete((res, exc) -> {
          if (exc != null) {
            LOG.error("Refinement execution in PI_ID [{}] failed, due to [{}]", piId,
                exc.getMessage());
          } else {
            LOG.info("Refinement execution in PI_ID [{}] sucessful", piId);
          }
        });
  }
}
