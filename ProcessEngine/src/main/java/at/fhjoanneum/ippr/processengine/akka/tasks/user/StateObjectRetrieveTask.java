package at.fhjoanneum.ippr.processengine.akka.tasks.user;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import at.fhjoanneum.ippr.commons.dto.processengine.StateDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.stateobject.BusinessObjectDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.stateobject.BusinessObjectFieldDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.stateobject.StateObjectDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.stateobject.SubjectDTO;
import at.fhjoanneum.ippr.persistence.entities.engine.enums.SubjectSubState;
import at.fhjoanneum.ippr.persistence.objects.engine.businessobject.BusinessObjectFieldInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.businessobject.BusinessObjectInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.state.SubjectState;
import at.fhjoanneum.ippr.persistence.objects.engine.subject.Subject;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.BusinessObjectModel;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.field.BusinessObjectFieldModel;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.permission.BusinessObjectFieldPermission;
import at.fhjoanneum.ippr.persistence.objects.model.enums.FieldPermission;
import at.fhjoanneum.ippr.persistence.objects.model.enums.StateEventType;
import at.fhjoanneum.ippr.persistence.objects.model.enums.StateFunctionType;
import at.fhjoanneum.ippr.persistence.objects.model.enums.SubjectModelType;
import at.fhjoanneum.ippr.persistence.objects.model.enums.TransitionType;
import at.fhjoanneum.ippr.persistence.objects.model.messageflow.MessageFlow;
import at.fhjoanneum.ippr.persistence.objects.model.process.ProcessModel;
import at.fhjoanneum.ippr.persistence.objects.model.state.State;
import at.fhjoanneum.ippr.persistence.objects.model.subject.SubjectModel;
import at.fhjoanneum.ippr.persistence.objects.model.transition.Transition;
import at.fhjoanneum.ippr.processengine.akka.messages.process.workflow.StateObjectMessage;
import at.fhjoanneum.ippr.processengine.akka.tasks.AbstractTask;
import at.fhjoanneum.ippr.processengine.composer.DbValueComposer;
import at.fhjoanneum.ippr.processengine.repositories.BusinessObjectFieldPermissionRepository;
import at.fhjoanneum.ippr.processengine.repositories.BusinessObjectInstanceRepository;
import at.fhjoanneum.ippr.processengine.repositories.SubjectRepository;
import at.fhjoanneum.ippr.processengine.repositories.SubjectStateRepository;

@Component("User.StateObjectRetrieveTask")
@Scope("prototype")
public class StateObjectRetrieveTask extends AbstractTask<StateObjectMessage.Request> {

  private final static Logger LOG = LoggerFactory.getLogger(StateObjectRetrieveTask.class);

  @Autowired
  private SubjectStateRepository subjectStateRepository;
  @Autowired
  private BusinessObjectFieldPermissionRepository businessObjectFieldPermissionRepository;
  @Autowired
  private BusinessObjectInstanceRepository businessObjectInstanceRepository;
  @Autowired
  private SubjectRepository subjectRepository;
  @Autowired
  private DbValueComposer valueComposer;

  @Override
  public boolean canHandle(final Object obj) {
    return obj instanceof StateObjectMessage.Request;
  }

  @Override
  public void execute(final StateObjectMessage.Request request) throws Exception {
    final SubjectState subjectState = Optional
        .ofNullable(
            subjectStateRepository.getSubjectStateOfUser(request.getPiId(), request.getUserId()))
        .get();

    if ((StateFunctionType.RECEIVE.equals(subjectState.getCurrentState().getFunctionType())
        && SubjectSubState.TO_RECEIVE.equals(subjectState.getSubState()))
        || (!request.isInternal() && StateFunctionType.REFINEMENT
            .equals(subjectState.getCurrentState().getFunctionType()))) {
      LOG.info("Nothing to do here for [{}]", subjectState);
      getSender().tell(new StateObjectMessage.Response(null), getSelf());
      return;
    }

    final List<BusinessObjectDTO> businessObjects = getBusinessObjects(request, subjectState);

    final List<StateDTO> nextStates = Lists.newArrayList(getNextStates(subjectState));

    StateObjectDTO stateObjectDTO = null;
    if (StateFunctionType.SEND.equals(subjectState.getCurrentState().getFunctionType())) {
      final List<SubjectDTO> subjects = Lists.newArrayList();

      // internal
      subjects.addAll(subjectState.getCurrentState().getMessageFlow().stream()
          .filter(messageFlow -> messageFlow.getReceiver().getSubjectModelType()
              .equals(SubjectModelType.INTERNAL))
          .map(messageFlow -> getAssignedUser(messageFlow, request.getPiId()))
          .collect(Collectors.toList()));

      // process
      subjects.addAll(subjectState.getCurrentState().getMessageFlow().stream()
          .filter(messageFlow -> messageFlow.getReceiver().getSubjectModelType()
              .equals(SubjectModelType.PROCESS))
          .map(mf -> getAssignedProcessUser(mf, request.getPiId())).collect(Collectors.toList()));

      stateObjectDTO = new StateObjectDTO(request.getPiId(), subjectState.getSsId(),
          businessObjects, nextStates, subjects);
    } else {
      stateObjectDTO = new StateObjectDTO(request.getPiId(), subjectState.getSsId(),
          businessObjects, nextStates);
    }

    getSender().tell(new StateObjectMessage.Response(stateObjectDTO), getSelf());
  }

  private List<BusinessObjectDTO> getBusinessObjects(final StateObjectMessage.Request request,
      final SubjectState subjectState) {
    final List<BusinessObjectDTO> businessObjects = Lists.newArrayList();

    for (final BusinessObjectModel businessObjectModel : subjectState.getCurrentState()
        .getBusinessObjectModels()) {
      final BusinessObjectDTO businessObjectDTO =
          getBusinessObjectDTO(request, businessObjectModel, subjectState);
      if (businessObjectDTO.hasNonEmptyFields()) {
        businessObjects.add(businessObjectDTO);
      }
    }
    return businessObjects;
  }

  private BusinessObjectDTO getBusinessObjectDTO(final StateObjectMessage.Request request,
      final BusinessObjectModel businessObjectModel, final SubjectState subjectState) {
    LOG.debug("Found business object model: {}", businessObjectModel);

    final Optional<BusinessObjectInstance> businessObjectInstanceOpt = Optional
        .ofNullable(businessObjectInstanceRepository.getBusinessObjectInstanceOfModelInProcess(
            request.getPiId(), businessObjectModel.getBomId()));

    if (!businessObjectInstanceOpt.isPresent()) {
      LOG.debug("No business object instance is present for BOM_ID [{}] in process PI_ID [{}]",
          businessObjectModel.getBomId(), request.getPiId());
    }

    final List<BusinessObjectFieldDTO> fields =
        getBusinessObjectFieldDTO(businessObjectModel, subjectState, businessObjectInstanceOpt);

    // get children
    final List<BusinessObjectDTO> children = businessObjectModel.getChildren().stream()
        .map(child -> getBusinessObjectDTO(request, child, subjectState))
        .collect(Collectors.toList());

    return new BusinessObjectDTO(businessObjectModel.getBomId(),
        businessObjectInstanceOpt.isPresent() ? businessObjectInstanceOpt.get().getBoiId() : null,
        businessObjectModel.getName(), fields, children);
  }

  private List<BusinessObjectFieldDTO> getBusinessObjectFieldDTO(
      final BusinessObjectModel businessObjectModel, final SubjectState subjectState,
      final Optional<BusinessObjectInstance> businessObjectInstanceOpt) {

    final List<BusinessObjectFieldDTO> fields = Lists.newArrayList();

    for (final BusinessObjectFieldModel businessObjectFieldModel : businessObjectModel
        .getBusinessObjectFieldModels()) {
      final BusinessObjectFieldPermission businessObjectFieldPermission =
          businessObjectFieldPermissionRepository.getBusinessObjectFieldPermissionInState(
              businessObjectFieldModel.getBofmId(), subjectState.getCurrentState().getSId());

      if (businessObjectFieldPermission == null) {
        throw new IllegalStateException(
            "Could not find permission for business object field model ["
                + businessObjectFieldModel.getBofmId() + "]");
      }

      final FieldPermission permission = businessObjectFieldPermission.getPermission();

      if (!FieldPermission.NONE.equals(permission)) {
        final Long bofmId = businessObjectFieldModel.getBofmId();
        final String name = businessObjectFieldModel.getFieldName();
        final String type = businessObjectFieldModel.getFieldType().name();
        final boolean required = businessObjectFieldPermission.isMandatory();
        final boolean readOnly =
            businessObjectFieldPermission.getPermission().equals(FieldPermission.READ) ? true
                : false;
        final int indent = businessObjectFieldModel.getIndent();
        Optional<BusinessObjectFieldInstance> fieldInstanceOpt = Optional.empty();

        String value = null;
        Long bofiId = null;
        if (businessObjectInstanceOpt.isPresent()) {
          fieldInstanceOpt = businessObjectInstanceOpt.get()
              .getBusinessObjectFieldInstanceOfFieldModel(businessObjectFieldModel);

          if (fieldInstanceOpt.isPresent()) {
            final BusinessObjectFieldInstance fieldInstance = fieldInstanceOpt.get();
            bofiId = fieldInstance.getBofiId();

            value = valueComposer.compose(fieldInstance.getValue(),
                fieldInstance.getBusinessObjectFieldModel().getFieldType());
          }
        }

        if (FieldPermission.READ_WRITE.equals(permission)) {
          value =
              StringUtils.isNotBlank(value) ? value : businessObjectFieldModel.getDefaultValue();
        }

        if (FieldPermission.READ_WRITE.equals(permission)
            || (FieldPermission.READ.equals(permission) && fieldInstanceOpt.isPresent())) {
          fields.add(new BusinessObjectFieldDTO(bofmId, bofiId, name, type, required, readOnly,
              value, indent));
        }
      } else {
        LOG.debug("Not necessary to add field [{}] since permission is 'NONE'",
            businessObjectFieldModel);
        continue;
      }
    }
    return fields;
  }

  private SubjectDTO getAssignedUser(final MessageFlow messageFlow, final Long piId) {
    final SubjectModel subjectModel = messageFlow.getReceiver();
    final Subject subject =
        subjectRepository.getSubjectForSubjectModelInProcess(piId, subjectModel.getSmId());
    return new SubjectDTO(subjectModel.getSmId(), subject.getUser(), subjectModel.getName(),
        subjectModel.getAssignedRules());
  }

  private SubjectDTO getAssignedProcessUser(final MessageFlow mf, final Long piID) {
    if (mf.getAssignedProcessModel().isPresent()) {
      final ProcessModel pm = mf.getAssignedProcessModel().get();
      final SubjectModel sm = pm.getStarterSubjectModel();
      return new SubjectDTO(sm.getSmId(), null, sm.getName(), sm.getAssignedRules());
    }

    return null;
  }

  private Set<StateDTO> getNextStates(final SubjectState subjectState) {
    Set<StateDTO> nextStates = Sets.newHashSet();
    final State currentState = subjectState.getCurrentState();

    if (filteringNeeded(subjectState)) {
      LOG.debug("Special check is necessary to return next states for [{}] ", currentState);
      final Set<BusinessObjectModel> retrievedBoms =
          Sets.newHashSet(subjectState.getCurrentMessageFlow().getBusinessObjectModels());
      final List<Set<BusinessObjectModel>> possibleRetrievedBoms =
          subjectState.getCurrentState().getMessageFlow().stream().map(mf -> {
            final Set<BusinessObjectModel> boms = Sets.newHashSet(mf.getBusinessObjectModels());
            return boms;
          }).collect(Collectors.toList());

      nextStates = subjectState.getCurrentState().getToStates().stream()
          .filter(transition -> checkIfIncludedForNextStates(possibleRetrievedBoms, retrievedBoms,
              transition))
          .map(transition -> new StateDTO(transition.getToState().getSId(),
              transition.getToState().getName(),
              StateEventType.END.equals(transition.getToState().getEventType())))
          .collect(Collectors.toSet());

      if (nextStates.isEmpty()) {
        LOG.warn("Could not find possible next states for [{}], add all states", subjectState);
        nextStates = currentState.getToStates().stream()
            .map(transition -> new StateDTO(transition.getToState().getSId(),
                transition.getToState().getName(),
                StateEventType.END.equals(transition.getToState().getEventType())))
            .collect(Collectors.toSet());
      }
    } else {
      LOG.debug("No special check is necessary, so return all states for [{}]", currentState);
      nextStates = currentState.getToStates().stream()
          .map(transition -> new StateDTO(transition.getToState().getSId(),
              transition.getToState().getName(),
              StateEventType.END.equals(transition.getToState().getEventType())))
          .collect(Collectors.toSet());
    }

    LOG.info("Possible next states are {} for [{}]", nextStates, subjectState);
    return nextStates;
  }

  private boolean filteringNeeded(final SubjectState subjectState) {
    if (StateFunctionType.RECEIVE.equals(subjectState.getCurrentState().getFunctionType())
        && subjectState.getCurrentState().getToStates().stream()
            .filter(transition -> TransitionType.NORMAL.equals(transition.getTransitionType()))
            .count() >= 2) {
      return true;
    }
    return false;
  }

  private boolean checkIfIncludedForNextStates(
      final List<Set<BusinessObjectModel>> possibleRetrievedBoms,
      final Set<BusinessObjectModel> retrievedBoms, final Transition transition) {
    final Set<BusinessObjectModel> stateBusinessObjectModels =
        Sets.newHashSet(transition.getToState().getBusinessObjectModels());
    possibleRetrievedBoms.removeAll(retrievedBoms);
    if (Sets.difference(retrievedBoms, stateBusinessObjectModels).isEmpty()) {
      LOG.debug("Retrieved BOMs {} are part of {}, therefore, [{}] is part of next state",
          retrievedBoms, stateBusinessObjectModels, transition.getToState());
      return true;
    } else if (canBeIncluded(possibleRetrievedBoms, stateBusinessObjectModels, transition)) {
      return true;
    } else {
      return false;
    }
  }

  private boolean canBeIncluded(final List<Set<BusinessObjectModel>> possibleRetrievedBoms,
      final Set<BusinessObjectModel> stateBusinessObjectModels, final Transition transition) {
    for (final Set<BusinessObjectModel> posBom : possibleRetrievedBoms) {
      if (Sets.difference(posBom, stateBusinessObjectModels).isEmpty()) {
        LOG.debug("BOMs {} are POSSIBLE part of {}, therfore [{}] is NO part of next state", posBom,
            stateBusinessObjectModels, transition.getToState());
        return false;
      }
    }
    return true;
  }
}
