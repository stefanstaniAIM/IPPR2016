package at.fhjoanneum.ippr.processengine.akka.actors.user;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.google.common.collect.Lists;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import at.fhjoanneum.ippr.commons.dto.processengine.StateDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.TaskDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.stateobject.BusinessObjectDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.stateobject.BusinessObjectFieldDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.stateobject.StateObjectDTO;
import at.fhjoanneum.ippr.persistence.entities.engine.enums.ReceiveSubjectState;
import at.fhjoanneum.ippr.persistence.entities.engine.state.SubjectStateBuilder;
import at.fhjoanneum.ippr.persistence.entities.engine.state.SubjectStateImpl;
import at.fhjoanneum.ippr.persistence.objects.engine.businessobject.BusinessObjectFieldInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.businessobject.BusinessObjectInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.enums.ProcessInstanceState;
import at.fhjoanneum.ippr.persistence.objects.engine.process.ProcessInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.state.SubjectState;
import at.fhjoanneum.ippr.persistence.objects.engine.subject.Subject;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.BusinessObjectModel;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.field.BusinessObjectFieldModel;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.permission.BusinessObjectFieldPermission;
import at.fhjoanneum.ippr.persistence.objects.model.enums.FieldPermission;
import at.fhjoanneum.ippr.persistence.objects.model.enums.StateFunctionType;
import at.fhjoanneum.ippr.persistence.objects.model.state.State;
import at.fhjoanneum.ippr.processengine.akka.config.Global;
import at.fhjoanneum.ippr.processengine.akka.messages.EmptyMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.info.TasksOfUserMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.initialize.UserActorInitializeMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.stop.ProcessStopMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.wakeup.UserActorWakeUpMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.workflow.StateObjectChangeMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.workflow.StateObjectMessage;
import at.fhjoanneum.ippr.processengine.akka.tasks.TaskManager;
import at.fhjoanneum.ippr.processengine.akka.tasks.TaskAllocation;
import at.fhjoanneum.ippr.processengine.composer.DbValueComposer;
import at.fhjoanneum.ippr.processengine.repositories.BusinessObjectFieldPermissionRepository;
import at.fhjoanneum.ippr.processengine.repositories.BusinessObjectInstanceRepository;
import at.fhjoanneum.ippr.processengine.repositories.CustomTypesQueriesRepository;
import at.fhjoanneum.ippr.processengine.repositories.ProcessInstanceRepository;
import at.fhjoanneum.ippr.processengine.repositories.StateRepository;
import at.fhjoanneum.ippr.processengine.repositories.SubjectRepository;
import at.fhjoanneum.ippr.processengine.repositories.SubjectStateRepository;

@Transactional
@Component("UserActor")
@Scope("prototype")
public class UserActor extends UntypedActor {

  private final static Logger LOG = LoggerFactory.getLogger(UserActor.class);

  @Autowired
  private ProcessInstanceRepository processInstanceRepository;
  @Autowired
  private SubjectRepository subjectRepository;
  @Autowired
  private StateRepository stateRepository;
  @Autowired
  private SubjectStateRepository subjectStateRepository;
  @Autowired
  private CustomTypesQueriesRepository customTypesQueriesRepository;
  @Autowired
  private BusinessObjectFieldPermissionRepository businessObjectFieldPermissionRepository;
  @Autowired
  private BusinessObjectInstanceRepository businessObjectInstanceRepository;

  @Autowired
  private DbValueComposer valueComposer;

  @Autowired
  private TaskManager taskManager;

  private final Long userId;

  public UserActor(final Long userId) {
    this.userId = userId;
  }

  @Override
  public void onReceive(final Object obj) throws Throwable {
    if (obj instanceof UserActorInitializeMessage.Request) {
      handleActorInitializeMessage(obj);
    } else if (obj instanceof UserActorWakeUpMessage.Request) {
      handleUserWakeUpMessage(obj);
    } else if (obj instanceof ProcessStopMessage.Request) {
      handleProcessStopMessage(obj);
    } else if (obj instanceof TasksOfUserMessage.Request) {
      handleTasksOfUserMessage(obj);
    } else if (obj instanceof StateObjectMessage.Request) {
      handleStateObjectMessage(obj);
    } else if (obj instanceof StateObjectChangeMessage.Request) {
      handleStateObjectChangeMessage(obj);
    } else {
      unhandled(obj);
    }
  }

  private void handleActorInitializeMessage(final Object obj) {
    final UserActorInitializeMessage.Request msg = (UserActorInitializeMessage.Request) obj;

    final ProcessInstance processInstance =
        Optional.ofNullable(processInstanceRepository.findOne(msg.getPiId())).get();

    final Subject subject = Optional.ofNullable(subjectRepository.findOne(msg.getSId())).get();

    final State state = Optional
        .ofNullable(stateRepository.getStartStateOfSubject(subject.getSubjectModel().getSmId()))
        .get();

    ReceiveSubjectState receiveSubjectState = null;
    if (state.getFunctionType().equals(StateFunctionType.RECEIVE)) {
      receiveSubjectState = ReceiveSubjectState.TO_RECEIVE;
    }

    final SubjectState subjectState = new SubjectStateBuilder().processInstance(processInstance)
        .subject(subject).state(state).receiveSubjectState(receiveSubjectState).build();

    subjectStateRepository.save((SubjectStateImpl) subjectState);
    LOG.info("Subject is now in initial state: {}", subjectState);

    final ActorRef sender = getSender();
    TransactionSynchronizationManager
        .registerSynchronization(new TransactionSynchronizationAdapter() {
          @Override
          public void afterCommit() {
            // notify user supervisor actor
            sender.tell(new EmptyMessage(), getSelf());
          }
        });

    if (userId.longValue() == Global.DESTROY_ID.longValue()) {
      getContext().stop(getSelf());
    }
  }

  private void handleUserWakeUpMessage(final Object obj) {
    final UserActorWakeUpMessage.Request msg = (UserActorWakeUpMessage.Request) obj;
    getSender().tell(new UserActorWakeUpMessage.Request(msg.getUserId()), getSelf());
  }

  private void handleProcessStopMessage(final Object obj) {
    final Long amountOfActiveProcesses = processInstanceRepository
        .getAmountOfProcessesPerUser(ProcessInstanceState.ACTIVE.name(), userId);

    if (amountOfActiveProcesses.longValue() == 0) {
      getContext().stop(getSelf());
      LOG.info("No active processes anymore for user [{}], therefore shutdown actor",
          getSelf().path().name());
    }
  }

  private void handleTasksOfUserMessage(final Object obj) {
    final TasksOfUserMessage.Request msg = (TasksOfUserMessage.Request) obj;

    final List<TaskDTO> tasks = customTypesQueriesRepository.getTasksOfUser(msg.getUserId());
    getSender().tell(new TasksOfUserMessage.Response(tasks), getSelf());
  }

  private void handleStateObjectMessage(final Object obj) {
    final StateObjectMessage.Request request = (StateObjectMessage.Request) obj;

    final SubjectState subjectState =
        Optional
            .ofNullable(subjectStateRepository
                .getSubjectStateOfUserInProcessInstance(request.getPiId(), request.getUserId()))
            .get();

    final List<BusinessObjectDTO> businessObjects = Lists.newArrayList();

    for (final BusinessObjectModel businessObjectModel : subjectState.getCurrentState()
        .getBusinessObjectModels()) {

      LOG.debug("Found business object model: {}", businessObjectModel);

      final List<BusinessObjectFieldDTO> fields = Lists.newArrayList();
      final Optional<BusinessObjectInstance> businessObjectInstanceOpt = Optional
          .ofNullable(businessObjectInstanceRepository.getBusinessObjectInstanceOfModelInProcess(
              request.getPiId(), businessObjectModel.getBomId()));

      if (!businessObjectInstanceOpt.isPresent()) {
        LOG.debug("No business object instance is present for BOM_ID [{}] in process PI_ID [{}]",
            businessObjectModel.getBomId(), request.getPiId());
      }

      for (final BusinessObjectFieldModel businessObjectFieldModel : businessObjectModel
          .getBusinessObjectFieldModels()) {
        final BusinessObjectFieldPermission businessObjectFieldPermission =
            businessObjectFieldPermissionRepository.getBusinessObjectFieldPermissionInState(
                businessObjectFieldModel.getBofmId(), subjectState.getCurrentState().getSId());

        if (!businessObjectFieldPermission.getPermission().equals(FieldPermission.NONE)) {
          final Long bofmId = businessObjectFieldModel.getBofmId();
          final String name = businessObjectFieldModel.getFieldName();
          final String type = businessObjectFieldModel.getFieldType().name();
          final boolean required = businessObjectFieldPermission.isMandatory();
          final boolean readOnly =
              businessObjectFieldPermission.getPermission().equals(FieldPermission.READ) ? true
                  : false;

          String value = null;
          Long bofiId = null;
          if (businessObjectInstanceOpt.isPresent()) {
            final Optional<BusinessObjectFieldInstance> fieldInstanceOpt = businessObjectInstanceOpt
                .get().getBusinessObjectFieldInstanceOfFieldModel(businessObjectFieldModel);

            if (fieldInstanceOpt.isPresent()) {
              final BusinessObjectFieldInstance fieldInstance = fieldInstanceOpt.get();
              bofiId = fieldInstance.getBofiId();

              value = valueComposer.compose(fieldInstance.getValue(),
                  fieldInstance.getBusinessObjectFieldModel().getFieldType());
            }
          }
          fields.add(
              new BusinessObjectFieldDTO(bofmId, bofiId, name, type, required, readOnly, value));
        } else {
          LOG.debug("Not necessary to add field [{}] since permission is 'NONE'");
          continue;
        }
      }
      businessObjects.add(new BusinessObjectDTO(businessObjectModel.getBomId(),
          businessObjectInstanceOpt.isPresent() ? businessObjectInstanceOpt.get().getBoiId() : null,
          businessObjectModel.getName(), fields));
    }

    final List<StateDTO> nextStates = subjectState.getCurrentState().getToStates().stream()
        .map(state -> new StateDTO(state.getToState().getSId(), state.getToState().getName()))
        .collect(Collectors.toList());

    getSender().tell(new StateObjectMessage.Response(
        new StateObjectDTO(request.getPiId(), subjectState.getSsId(), businessObjects, nextStates)),
        getSelf());
  }

  private void handleStateObjectChangeMessage(final Object obj) {
    taskManager.executeTask(TaskAllocation.STATE_OBJECT_CHANGE_TASK, getContext(), obj);
  }
}
