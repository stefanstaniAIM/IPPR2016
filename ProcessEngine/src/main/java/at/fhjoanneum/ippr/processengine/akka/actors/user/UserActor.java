package at.fhjoanneum.ippr.processengine.akka.actors.user;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import akka.actor.UntypedActor;
import at.fhjoanneum.ippr.commons.dto.processengine.SendProcessMessage;
import at.fhjoanneum.ippr.commons.dto.processengine.TaskDTO;
import at.fhjoanneum.ippr.persistence.objects.engine.enums.ProcessInstanceState;
import at.fhjoanneum.ippr.processengine.akka.config.Global;
import at.fhjoanneum.ippr.processengine.akka.messages.process.info.TasksOfUserMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.initialize.UserActorInitializeMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.refinement.ExecuteRefinementMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.stop.ProcessStopMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.timeout.TimeoutExecuteMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.timeout.TimeoutScheduleCancelMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.timeout.TimeoutScheduleStartMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.wakeup.UserActorWakeUpMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.workflow.AssignUsersMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.workflow.MessageReceiveMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.workflow.MessagesSendMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.workflow.StateObjectChangeMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.workflow.StateObjectMessage;
import at.fhjoanneum.ippr.processengine.akka.tasks.TaskAllocation;
import at.fhjoanneum.ippr.processengine.akka.tasks.TaskManager;
import at.fhjoanneum.ippr.processengine.repositories.CustomTypesQueriesRepository;
import at.fhjoanneum.ippr.processengine.repositories.ProcessInstanceRepository;

@Transactional(isolation = Isolation.READ_COMMITTED)
@Component("UserActor")
@Scope("prototype")
public class UserActor extends UntypedActor {

  private final static Logger LOG = LoggerFactory.getLogger(UserActor.class);

  @Autowired
  private ProcessInstanceRepository processInstanceRepository;
  @Autowired
  private CustomTypesQueriesRepository customTypesQueriesRepository;
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
    } else if (obj instanceof MessagesSendMessage.Request) {
      handleSendMessages(obj);
    } else if (obj instanceof MessageReceiveMessage.Request) {
      handleMessageReceived(obj);
    } else if (obj instanceof AssignUsersMessage.Request) {
      handleAssignUsersMessage(obj);
    } else if (obj instanceof ExecuteRefinementMessage.Request) {
      handleExecuteRefinementMessage(obj);
    } else if (obj instanceof TimeoutScheduleStartMessage) {
      handleTimeoutScheduleStartMessage(obj);
    } else if (obj instanceof TimeoutExecuteMessage) {
      handleTimeoutExecuteMessage(obj);
    } else if (obj instanceof TimeoutScheduleCancelMessage) {
      handleTimeoutCancelMessage(obj);
    } else if (obj instanceof SendProcessMessage.Request) {
      handleSendProcessMessage(obj);
    } else {
      LOG.warn("Unhandled message: {}", obj);
      unhandled(obj);
    }
  }

  private void handleActorInitializeMessage(final Object obj) {
    taskManager.executeTask(TaskAllocation.USER_ACTOR_INITIALIZE_TASK, getContext(), obj,
        result -> {
          if (userId.longValue() == Global.DESTROY_ID.longValue()) {
            getContext().stop(getSelf());
          }
        });
  }

  private void handleUserWakeUpMessage(final Object obj) {
    final UserActorWakeUpMessage.Request msg = (UserActorWakeUpMessage.Request) obj;
    getSender().tell(new UserActorWakeUpMessage.Request(msg.getUserId()), getSelf());
  }

  private void handleProcessStopMessage(final Object obj) {
    final Long amountOfActiveProcesses = processInstanceRepository
        .getAmountOfProcessesInStatePerUser(ProcessInstanceState.ACTIVE.name(), userId);

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
    taskManager.executeTask(TaskAllocation.STATE_OBJECT_RETRIEVE_TASK, getContext(), obj);
  }

  private void handleStateObjectChangeMessage(final Object obj) {
    taskManager.executeTask(TaskAllocation.STATE_OBJECT_CHANGE_TASK, getContext(), obj);
  }

  private void handleSendMessages(final Object obj) {
    getContext().parent().forward(obj, getContext());
  }

  private void handleMessageReceived(final Object obj) {
    taskManager.executeTask(TaskAllocation.MESSAGE_RECEIVED_TASK, getContext(), obj);
  }

  private void handleAssignUsersMessage(final Object obj) {
    getContext().parent().forward(obj, getContext());
  }

  private void handleExecuteRefinementMessage(final Object obj) {
    taskManager.executeTask(TaskAllocation.EXECUTE_REFINEMENT_TASK, getContext(), obj);
  }

  private void handleTimeoutScheduleStartMessage(final Object obj) {
    taskManager.executeTaskInContext(TaskAllocation.START_TIMEOUT_TASK, getContext(), obj);
  }

  private void handleTimeoutExecuteMessage(final Object obj) {
    taskManager.executeTask(TaskAllocation.EXECUTE_TIMEOUT_TASK, getContext(), obj);
  }

  private void handleTimeoutCancelMessage(final Object obj) {
    taskManager.executeTaskInContext(TaskAllocation.CANCEL_TIMEOUT_TASK, getContext(), obj);
  }

  private void handleSendProcessMessage(final Object obj) {
    taskManager.executeTask(TaskAllocation.SEND_PROCESS_MESSAGE_TASK, getContext(), obj);
  }
}
