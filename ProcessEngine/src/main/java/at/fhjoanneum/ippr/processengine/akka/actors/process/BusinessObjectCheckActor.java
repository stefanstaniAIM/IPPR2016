package at.fhjoanneum.ippr.processengine.akka.actors.process;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

import akka.actor.UntypedActor;
import at.fhjoanneum.ippr.commons.dto.processengine.stateobject.BusinessObjectFieldInstanceDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.stateobject.BusinessObjectInstanceDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.stateobject.UserAssignmentDTO;
import at.fhjoanneum.ippr.persistence.objects.engine.subject.Subject;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.BusinessObjectModel;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.field.BusinessObjectFieldModel;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.permission.BusinessObjectFieldPermission;
import at.fhjoanneum.ippr.persistence.objects.model.enums.FieldPermission;
import at.fhjoanneum.ippr.persistence.objects.model.enums.StateFunctionType;
import at.fhjoanneum.ippr.persistence.objects.model.enums.SubjectModelType;
import at.fhjoanneum.ippr.persistence.objects.model.messageflow.MessageFlow;
import at.fhjoanneum.ippr.persistence.objects.model.state.State;
import at.fhjoanneum.ippr.persistence.objects.model.subject.SubjectModel;
import at.fhjoanneum.ippr.processengine.akka.messages.process.workflow.StateObjectChangeMessage;
import at.fhjoanneum.ippr.processengine.parser.DbValueParser;
import at.fhjoanneum.ippr.processengine.repositories.BusinessObjectFieldPermissionRepository;
import at.fhjoanneum.ippr.processengine.repositories.StateRepository;
import at.fhjoanneum.ippr.processengine.repositories.SubjectRepository;

@Transactional
@Component("BusinessObjectCheckActor")
@Scope("prototype")
public class BusinessObjectCheckActor extends UntypedActor {

  private final static Logger LOG = LoggerFactory.getLogger(BusinessObjectCheckActor.class);

  @Autowired
  private SubjectRepository subjectRepository;
  @Autowired
  private StateRepository stateRepository;
  @Autowired
  private BusinessObjectFieldPermissionRepository businessObjectFieldPermissionRepository;
  @Autowired
  private DbValueParser parser;

  private final Long currentState;

  public BusinessObjectCheckActor(final Long currentState) {
    this.currentState = currentState;
  }

  @Override
  public void onReceive(final Object obj) throws Throwable {
    if (obj instanceof StateObjectChangeMessage.Request) {
      handleStateObjectChangeMessage(obj);
    } else {
      unhandled(obj);
    }
  }

  private void handleStateObjectChangeMessage(final Object obj) {
    try {
      final StateObjectChangeMessage.Request request = (StateObjectChangeMessage.Request) obj;

      boolean correct = true;

      final State state = stateRepository.findOne(currentState);

      if (!checkSendState(state, request)) {
        getSender().tell(Boolean.FALSE, getSelf());
        return;
      }

      // retrieve all business object models - also childs
      final List<BusinessObjectModel> businessObjectModels =
          state.getBusinessObjectModels().stream().map(BusinessObjectModel::flattened)
              .flatMap(List::stream).collect(Collectors.toList());

      final List<BusinessObjectFieldModel> fieldModels =
          businessObjectModels.stream().map(BusinessObjectModel::getBusinessObjectFieldModels)
              .flatMap(List::stream).collect(Collectors.toList());

      final Map<Long, String> fieldInstances =
          convertToMap(request.getStateObjectChangeDTO().getBusinessObjects());

      for (final BusinessObjectFieldModel fieldModel : fieldModels) {
        correct = checkField(fieldModel, fieldInstances.get(fieldModel.getBofmId()));
        if (!correct) {
          LOG.info("Result of business object check was false");
          getSender().tell(Boolean.FALSE, getSelf());
          return;
        }
      }

      LOG.info("Result of business object check was true");
      getSender().tell(Boolean.TRUE, getSelf());
    } finally {
      getContext().stop(getSelf());
    }
  }

  private boolean checkSendState(final State state,
      final StateObjectChangeMessage.Request request) {
    if (state.getFunctionType().equals(StateFunctionType.SEND)) {

      final List<MessageFlow> missingUsers = state.getMessageFlow().stream()
          .filter(mf -> SubjectModelType.INTERNAL.equals(mf.getReceiver().getSubjectModelType()))
          .filter(messageFlow -> isUserIdNotAssigned(messageFlow, request.getPiId()))
          .collect(Collectors.toList());

      if (missingUsers.isEmpty()) {
        return true;
      }

      final List<UserAssignmentDTO> userAssignments =
          request.getStateObjectChangeDTO().getUserAssignments();
      if (userAssignments == null || userAssignments.isEmpty()) {
        LOG.error("User assignments are empty!");
        return false;
      }

      final Map<Long, Long> userAssignmentsMap = userAssignments.stream()
          .collect(Collectors.toMap(UserAssignmentDTO::getSmId, UserAssignmentDTO::getUserId));

      final long count = missingUsers.stream().filter(messageFlow -> {
        final SubjectModel receiver = messageFlow.getReceiver();
        if (userAssignmentsMap.containsKey(receiver.getSmId())
            && userAssignmentsMap.get(receiver.getSmId()) != null) {
          LOG.debug("Could find user assignment for SM_ID [{}]", receiver.getSmId());
          return true;
        } else {
          LOG.error("Could not find user assignment for SM_ID [{}]", receiver.getSmId());
          return false;
        }
      }).count();

      return missingUsers.size() == count;
    }
    return true;
  }

  private boolean isUserIdNotAssigned(final MessageFlow messageFlow, final Long piId) {
    final Optional<Subject> subjectOpt = Optional.ofNullable(subjectRepository
        .getSubjectForSubjectModelInProcess(piId, messageFlow.getReceiver().getSmId()));
    if (!subjectOpt.isPresent()) {
      LOG.error("No subject present for SM_ID [{}] in PI_ID [{}]",
          messageFlow.getReceiver().getSmId(), piId);
      throw new IllegalStateException();
    }

    final Subject subject = subjectOpt.get();
    if (subject.getUser() != null) {
      return false;
    }
    LOG.debug("UserId of subject [{}] is not assigned and must be include in user assignments",
        subject);
    return true;
  }

  private Map<Long, String> convertToMap(final List<BusinessObjectInstanceDTO> businessObjects) {
    final Map<Long, String> map = Maps.newHashMap();

    if (businessObjects == null || businessObjects.isEmpty()) {
      return map;
    }

    final Stream<BusinessObjectInstanceDTO> allBusinessObjects =
        businessObjects.stream().map(BusinessObjectInstanceDTO::flattened).flatMap(List::stream);

    return allBusinessObjects.map(BusinessObjectInstanceDTO::getFields).flatMap(List::stream)
        .collect(Collectors.toMap(BusinessObjectFieldInstanceDTO::getBofmId,
            BusinessObjectFieldInstanceDTO::getValue));
  }

  private boolean checkField(final BusinessObjectFieldModel fieldModel, final String fieldValue) {
    final BusinessObjectFieldPermission fieldPermission = businessObjectFieldPermissionRepository
        .getBusinessObjectFieldPermissionInState(fieldModel.getBofmId(), currentState);

    if (fieldPermission == null) {
      LOG.warn("Could not find field permission in state S_ID [{}] for field model BOFM_ID [{}]",
          currentState, fieldModel.getBofmId());
      return true;
    }

    if (fieldPermission.getPermission().equals(FieldPermission.READ_WRITE)) {
      if (fieldPermission.isMandatory() && StringUtils.isBlank(fieldValue)) {
        LOG.error("Field model BOFM_ID [{}] is mandatory but is empty", fieldModel.getBofmId());
        return false;
      } else if (!fieldPermission.isMandatory() && StringUtils.isBlank(fieldValue)) {
        return true;
      }

      final boolean canParse = parser.canParse(fieldValue, fieldModel.getFieldType());
      if (!canParse) {
        LOG.error("Could not parse value [{}] to type [{}]", fieldValue, fieldModel.getFieldType());
      }
      return canParse;
    }
    return true;
  }
}
