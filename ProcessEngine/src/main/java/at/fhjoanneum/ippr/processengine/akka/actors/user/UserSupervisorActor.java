package at.fhjoanneum.ippr.processengine.akka.actors.user;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import at.fhjoanneum.ippr.persistence.objects.engine.process.ProcessInstance;
import at.fhjoanneum.ippr.processengine.akka.AkkaSelector;
import at.fhjoanneum.ippr.processengine.akka.config.SpringExtension;
import at.fhjoanneum.ippr.processengine.akka.messages.process.info.TasksOfUserMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.initialize.ActorInitializeMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.stop.ProcessStopMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.timeout.TimeoutScheduleStartMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.wakeup.UserActorWakeUpMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.workflow.AssignUsersMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.workflow.MessagesSendMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.workflow.StateObjectChangeMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.workflow.StateObjectMessage;
import at.fhjoanneum.ippr.processengine.akka.tasks.TaskAllocation;
import at.fhjoanneum.ippr.processengine.akka.tasks.TaskManager;
import at.fhjoanneum.ippr.processengine.repositories.ProcessInstanceRepository;

@Transactional(isolation = Isolation.READ_COMMITTED)
@Component("UserSupervisorActor")
@Scope("prototype")
public class UserSupervisorActor extends UntypedActor {

  private final static Logger LOG = LoggerFactory.getLogger(UserSupervisorActor.class);

  @Autowired
  private AkkaSelector akkaSelector;

  @Autowired
  private TaskManager taskManager;

  @Autowired
  private ProcessInstanceRepository processInstanceRepository;

  @Autowired
  private SpringExtension springExtension;

  @Override
  public void onReceive(final Object obj) throws Throwable {
    if (obj instanceof ActorInitializeMessage.Request) {
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
    } else if (obj instanceof AssignUsersMessage.Request) {
      handleAssignUsersMessage(obj);
    } else if (obj instanceof TimeoutScheduleStartMessage) {
      handleTimeoutScheduleStartMessage(obj);
    } else {
      LOG.warn("Unhandled message: {}", obj);
      unhandled(obj);
    }
  }

  private void handleActorInitializeMessage(final Object obj) {
    taskManager.executeTaskInContext(TaskAllocation.PROCESS_INITIALIZE_TASK, getContext(), obj);
  }

  private String getUserId(final Long userId) {
    return "ProcessUser-" + userId;
  }

  private void handleUserWakeUpMessage(final Object obj) {
    final UserActorWakeUpMessage.Request msg = (UserActorWakeUpMessage.Request) obj;
    final String userId = getUserId(msg.getUserId());
    final Optional<ActorRef> actorOpt = akkaSelector.findActor(getContext(), userId);

    ActorRef actor = null;
    if (!actorOpt.isPresent()) {
      actor = getContext().actorOf(springExtension.props("UserActor", msg.getUserId()), userId);
    } else {
      actor = actorOpt.get();
    }
    actor.forward(msg, getContext());
  }

  private void handleProcessStopMessage(final Object obj) {
    final ProcessStopMessage.Request msg = (ProcessStopMessage.Request) obj;
    final Optional<ProcessInstance> processOpt =
        Optional.ofNullable(processInstanceRepository.findOne(msg.getPiId()));

    if (processOpt.isPresent()) {
      final ProcessInstance process = processOpt.get();
      if (process.isStopped()) {
        final List<String> userIds =
            process.getSubjects().stream().filter(subject -> subject.getUser() != null)
                .map(subject -> "ProcessUser-" + subject.getUser()).collect(Collectors.toList());

        userIds.forEach(userId -> {
          final Optional<ActorRef> actorOpt = akkaSelector.findActor(getContext(), userId);
          if (actorOpt.isPresent()) {
            actorOpt.get().forward(msg, getContext());
          }
        });
      }
    }
  }

  private void handleTasksOfUserMessage(final Object obj) {
    final TasksOfUserMessage.Request msg = (TasksOfUserMessage.Request) obj;

    final String userId = getUserId(msg.getUserId());
    final Optional<ActorRef> actorOpt = akkaSelector.findActor(getContext(), userId);
    if (!actorOpt.isPresent()) {
      getSender().tell(new TasksOfUserMessage.Response(Lists.newArrayList()), getSelf());
      return;
    }

    actorOpt.get().forward(msg, getContext());
  }

  private void handleStateObjectMessage(final Object obj) {
    final StateObjectMessage.Request request = (StateObjectMessage.Request) obj;
    LOG.info("Handle state object message of USER_ID [{}] in PI_ID [{}]", request.getUserId(),
        request.getPiId());

    forwardToUserActor(request.getUserId(), request);
  }

  private <T> void forwardToUserActor(final Long userId, final T msg) {
    final String actorUserId = getUserId(userId);
    final Optional<ActorRef> actorOpt = akkaSelector.findActor(getContext(), actorUserId);

    if (actorOpt.isPresent()) {
      LOG.debug("Found user actor and will forward message");
      actorOpt.get().forward(msg, getContext());
    } else {
      getSender().tell(
          new akka.actor.Status.Failure(
              new IllegalArgumentException("Could not find actor for user: " + actorUserId)),
          getSelf());
      return;
    }
  }

  private void handleStateObjectChangeMessage(final Object obj) {
    final StateObjectChangeMessage.Request request = (StateObjectChangeMessage.Request) obj;
    forwardToUserActor(request.getUserId(), request);
  }

  private void handleSendMessages(final Object obj) {
    taskManager.executeTaskInContext(TaskAllocation.SEND_MESSAGES_TASK, getContext(), obj);
  }

  private void handleAssignUsersMessage(final Object obj) {
    taskManager.executeTask(TaskAllocation.ASSIGN_USERS_TASK, getContext(), obj);
  }

  private void handleTimeoutScheduleStartMessage(final Object obj) {
    final TimeoutScheduleStartMessage msg = (TimeoutScheduleStartMessage) obj;
    forwardToUserActor(msg.getUserId(), msg);
  }
}
