package at.fhjoanneum.ippr.pmstorage.services.impl;

import at.fhjoanneum.ippr.commons.dto.owlimport.jsonimport.ImportBusinessObjectFieldsModelDTO;
import at.fhjoanneum.ippr.commons.dto.owlimport.jsonimport.ImportProcessModelDTO;
import at.fhjoanneum.ippr.commons.dto.owlimport.reader.OWLProcessModelDTO;
import at.fhjoanneum.ippr.persistence.entities.model.businessobject.BusinessObjectModelBuilder;
import at.fhjoanneum.ippr.persistence.entities.model.businessobject.field.BusinessObjectFieldModelBuilder;
import at.fhjoanneum.ippr.persistence.entities.model.businessobject.permission.BusinessObjectFieldPermissionBuilder;
import at.fhjoanneum.ippr.persistence.entities.model.messageflow.MessageFlowBuilder;
import at.fhjoanneum.ippr.persistence.entities.model.process.ProcessModelBuilder;
import at.fhjoanneum.ippr.persistence.entities.model.state.StateBuilder;
import at.fhjoanneum.ippr.persistence.entities.model.subject.SubjectModelBuilder;
import at.fhjoanneum.ippr.persistence.entities.model.transition.TransitionBuilder;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.BusinessObjectModel;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.field.BusinessObjectFieldModel;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.permission.BusinessObjectFieldPermission;
import at.fhjoanneum.ippr.persistence.objects.model.enums.*;
import at.fhjoanneum.ippr.persistence.objects.model.messageflow.MessageFlow;
import at.fhjoanneum.ippr.persistence.objects.model.process.ProcessModel;
import at.fhjoanneum.ippr.persistence.objects.model.state.State;
import at.fhjoanneum.ippr.persistence.objects.model.subject.SubjectModel;
import at.fhjoanneum.ippr.persistence.objects.model.transition.Transition;
import at.fhjoanneum.ippr.pmstorage.parser.OWLParser;
import at.fhjoanneum.ippr.pmstorage.services.OwlImportService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

@Transactional
@Service
public class OwlImportServiceImpl implements OwlImportService {

  private final static Logger LOG = LoggerFactory.getLogger(OwlImportServiceImpl.class);

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private OWLParser owlParser;

  @Async
  @Override
  public Future<OWLProcessModelDTO> getOwlProcessModelDTO(final String owlContent, final String version) {
    final OWLProcessModelDTO owlProcessModel = owlParser.parseFile(owlContent, version);
    return new AsyncResult<OWLProcessModelDTO>(owlProcessModel);
  }

  @Async
  @Override
  public Future<Boolean> importProcessModel(final ImportProcessModelDTO processModelDTO) {
    try {
      final ProcessModelBuilder pmBuilder =
          new ProcessModelBuilder().name(processModelDTO.getName()).state(ProcessModelState.ACTIVE)
              .description(processModelDTO.getDescription()).version(processModelDTO.getVersion());

      final Map<String, SubjectModel> subjectModelMap = Maps.newHashMap();

      processModelDTO.getSubjectModels().stream().forEachOrdered(subjectModelDTO -> {
        final SubjectModelBuilder smBuilder =
            new SubjectModelBuilder().name(subjectModelDTO.getName());
        subjectModelDTO.getAssignedRules().stream()
            .forEachOrdered(rule -> smBuilder.addAssignedRule(rule));

        final SubjectModel subjectModel = smBuilder.build();
        subjectModelMap.put(subjectModelDTO.getId(), subjectModel);
        pmBuilder.addSubjectModel(subjectModel);
      });

      pmBuilder.starterSubject(subjectModelMap.get(processModelDTO.getStartSubjectModelId()));

      final Map<String, State> stateMap = Maps.newHashMap();

      processModelDTO.getStates().stream().forEachOrdered(stateDTO -> {
        final StateBuilder stateBuilder = new StateBuilder().name(stateDTO.getName());
        if (StringUtils.isNotBlank(stateDTO.getEventType())) {
          stateBuilder.eventType(StateEventType.valueOf(stateDTO.getEventType()));
        }
        stateBuilder.functionType(StateFunctionType.valueOf(stateDTO.getFunctionType()));
        stateBuilder.subjectModel(subjectModelMap.get(stateDTO.getSubjectModelId()));

        final State state = stateBuilder.build();
        stateMap.put(stateDTO.getId(), state);
      });

      final List<Transition> transitions = Lists.newArrayList();

      processModelDTO.getTransitions().stream().forEach(transitionDTO -> {
        final TransitionBuilder tBuilder =
            new TransitionBuilder().fromState(stateMap.get(transitionDTO.getFromStateId()))
                .toState(stateMap.get(transitionDTO.getToStateId()));

        // TODO check transition type
        tBuilder.transitionType(TransitionType.NORMAL);

        transitions.add(tBuilder.build());
      });

      final Map<String, BusinessObjectModel> bomMap = Maps.newHashMap();

      processModelDTO.getBoms().stream().forEachOrdered(bomDTO -> {
        final BusinessObjectModelBuilder bomBuilder =
            new BusinessObjectModelBuilder().name(bomDTO.getName());
        bomDTO.getStateIds().stream().forEachOrdered(stateId -> {
          bomBuilder.addToState(stateMap.get(stateId));
        });

        final BusinessObjectModel bom = bomBuilder.build();
        bomMap.put(bomDTO.getId(), bom);
      });

      final Map<String, BusinessObjectFieldModel> bofmMap = Maps.newHashMap();

      IntStream.range(0, processModelDTO.getBofms().size()).forEach(idx -> {
        final ImportBusinessObjectFieldsModelDTO bofmDTO = processModelDTO.getBofms().get(idx);
        final BusinessObjectFieldModelBuilder builder =
            new BusinessObjectFieldModelBuilder().fieldName(bofmDTO.getName()).position(idx);
        builder.fieldType(FieldType.valueOf(bofmDTO.getType()));
        builder.businessObjectModel(bomMap.get(bofmDTO.getBomId()));

        final BusinessObjectFieldModel fieldModel = builder.build();
        bofmMap.put(bofmDTO.getId(), fieldModel);
      });

      final List<BusinessObjectFieldPermission> permissions = Lists.newArrayList();

      processModelDTO.getBofps().stream().forEachOrdered(bofpDTO -> {
        final BusinessObjectFieldPermissionBuilder builder =
            new BusinessObjectFieldPermissionBuilder();

        if (bofpDTO.isWrite()) {
          builder.permission(FieldPermission.READ_WRITE);
        } else if (bofpDTO.isRead()) {
          builder.permission(FieldPermission.READ);
        } else {
          builder.permission(FieldPermission.NONE);
        }

        builder.mandatory(bofpDTO.isMandatory());
        builder.businessObjectFieldModel(bofmMap.get(bofpDTO.getBofmId()));
        builder.state(stateMap.get(bofpDTO.getStateId()));
        permissions.add(builder.build());
      });

      final List<MessageFlow> messageFlows = Lists.newArrayList();

      processModelDTO.getMessageFlows().stream().forEachOrdered(messageFlowDTO -> {
        final MessageFlowBuilder builder = new MessageFlowBuilder();
        builder.sender(subjectModelMap.get(messageFlowDTO.getSenderId()));
        builder.receiver(subjectModelMap.get(messageFlowDTO.getReceiverId()));
        builder.assignBusinessObjectModel(bomMap.get(messageFlowDTO.getBomId()));

        builder.state(stateMap.get(messageFlowDTO.getSenderStateId()));
        messageFlows.add(builder.build());
      });

      saveSubjectModels(subjectModelMap.values());
      saveProcessModel(pmBuilder.build());
      saveStates(stateMap.values());
      saveTransitions(transitions);
      saveBusinessObjectModels(bomMap.values());
      saveBusinessObjectFieldModels(bofmMap.values());
      saveBusinessObjectFieldPermissions(permissions);
      saveMessageFlows(messageFlows);

      return new AsyncResult<Boolean>(Boolean.TRUE);
    } catch (final Exception e) {
      e.printStackTrace();
      return new AsyncResult<Boolean>(Boolean.FALSE);
    }
  }

  protected void saveProcessModel(final ProcessModel processModel) {
    entityManager.persist(processModel);
    LOG.info("Saved new process model: {}", processModel);
  }

  protected void saveSubjectModels(final Collection<SubjectModel> models) {
    for (final SubjectModel model : models) {
      entityManager.persist(model);
      LOG.info("Saved new subject model: {}", model);
    }
  }

  protected void saveStates(final Collection<State> states) {
    for (final State state : states) {
      entityManager.persist(state);
      LOG.info("Saved new state: {}", state);
    }
  }

  protected void saveMessageFlows(final Collection<MessageFlow> messageFlows) {
    for (final MessageFlow messageFlow : messageFlows) {
      entityManager.persist(messageFlow);
      LOG.info("Saved new message flow: {}", messageFlow);
    }
  }

  protected void saveTransitions(final Collection<Transition> transitions) {
    for (final Transition transition : transitions) {
      entityManager.persist(transition);
      LOG.info("Saved new transition: {}", transition);
    }
  }

  protected void saveBusinessObjectModels(
      final Collection<BusinessObjectModel> businessObjectModels) {
    for (final BusinessObjectModel businessObjectModel : businessObjectModels) {
      entityManager.persist(businessObjectModel);
      LOG.info("Saved new business object model: {}", businessObjectModel);
    }
  }

  protected void saveBusinessObjectFieldModels(
      final Collection<BusinessObjectFieldModel> businessObjectFieldModels) {
    for (final BusinessObjectFieldModel businessObjectFieldModel : businessObjectFieldModels) {
      entityManager.persist(businessObjectFieldModel);
      LOG.info("Saved new business object field model: {}", businessObjectFieldModel);
    }
  }

  protected void saveBusinessObjectFieldPermissions(
      final Collection<BusinessObjectFieldPermission> businessObjectFieldPermissions) {
    for (final BusinessObjectFieldPermission businessObjectFieldPermission : businessObjectFieldPermissions) {
      entityManager.persist(businessObjectFieldPermission);
      LOG.info("Saved new business object field permission: {}", businessObjectFieldPermission);
    }
  }
}
