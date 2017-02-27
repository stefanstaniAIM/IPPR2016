package at.fhjoanneum.ippr.processengine.akka.actors.process;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import at.fhjoanneum.ippr.processengine.akka.AkkaSelector;
import at.fhjoanneum.ippr.processengine.akka.config.SpringExtension;
import at.fhjoanneum.ippr.processengine.akka.messages.process.info.ProcessInfoMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.info.ProcessStateMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.initialize.ProcessStartMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.stop.ProcessStopMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.wakeup.ProcessWakeUpMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.workflow.StateObjectChangeMessage;
import at.fhjoanneum.ippr.processengine.akka.tasks.TaskAllocation;
import at.fhjoanneum.ippr.processengine.akka.tasks.TaskManager;

@Transactional(isolation = Isolation.READ_COMMITTED)
@Component("ProcessSupervisorActor")
@Scope("prototype")
public class ProcessSupervisorActor extends UntypedActor {

  private final static Logger LOG = LoggerFactory.getLogger(ProcessSupervisorActor.class);

  @Autowired
  private TaskManager taskManager;

  @Autowired
  private AkkaSelector akkaSelector;
  @Autowired
  private SpringExtension springExtension;

  @Override
  public void onReceive(final Object obj) throws Throwable {
    if (obj instanceof ProcessStartMessage.Request) {
      handleProcessStartMessage(obj);
    } else if (obj instanceof ProcessStateMessage.Request) {
      handleProcessStateMessage(obj);
    } else if (obj instanceof ProcessWakeUpMessage.Request) {
      handleProcessWakeUpMessage(obj);
    } else if (obj instanceof ProcessInfoMessage.Request) {
      handleProcessInfoMessage(obj);
    } else if (obj instanceof ProcessStopMessage.Request) {
      handleProcessStopMessage(obj);
    } else if (obj instanceof StateObjectChangeMessage.Request) {
      handleStateObjectChangeMessage(obj);
    } else {
      LOG.warn("Unhandled message: {}", obj);
      unhandled(obj);
    }
  }

  private void handleProcessStartMessage(final Object obj) {
    taskManager.executeTask(TaskAllocation.PROCESS_START_TASK, getContext(), obj,
        (final Long piId) -> {
          final String processInstanceId = getProcessActorId(piId);
          final Optional<ActorRef> actorOpt =
              akkaSelector.findActor(getContext(), processInstanceId);
          if (!actorOpt.isPresent()) {
            getContext().actorOf(springExtension.props("ProcessActor", piId), processInstanceId);
            LOG.info("Created new actor for process instance: {}", processInstanceId);
          }
        });
  }

  private String getProcessActorId(final Long piId) {
    return "Process-" + piId;
  }

  private void handleProcessStateMessage(final Object obj) {
    final ProcessStateMessage.Request msg = (ProcessStateMessage.Request) obj;

    final String processInstanceId = getProcessActorId(msg.getPiId());
    final Optional<ActorRef> actorOpt = akkaSelector.findActor(getContext(), processInstanceId);

    if (!actorOpt.isPresent()) {
      final String error = "Could not find process actor for: " + processInstanceId;
      getSender().tell(new akka.actor.Status.Failure(new IllegalArgumentException(error)),
          getSelf());
      return;
    }

    actorOpt.get().forward(msg, getContext());
  }

  private void handleProcessWakeUpMessage(final Object obj) {
    final ProcessWakeUpMessage.Request msg = (ProcessWakeUpMessage.Request) obj;

    final String processInstanceId = getProcessActorId(msg.getPiId());
    final Optional<ActorRef> actorOpt = akkaSelector.findActor(getContext(), processInstanceId);

    if (!actorOpt.isPresent()) {
      getContext().actorOf(springExtension.props("ProcessActor", msg.getPiId()), processInstanceId);
      LOG.info("Created new actor for process instance after wake up message: {}",
          processInstanceId);
    }

    getSender().tell(new ProcessWakeUpMessage.Response(msg.getPiId()), getSelf());
  }

  private void handleProcessInfoMessage(final Object obj) {
    taskManager.executeTask(TaskAllocation.PROCESS_INFO_TASK, getContext(), obj);
  }

  private void handleProcessStopMessage(final Object obj) {
    final ProcessStopMessage.Request msg = (ProcessStopMessage.Request) obj;
    forwardToProcessActor(msg.getPiId(), msg);
  }

  private <T> void forwardToProcessActor(final Long piId, final T msg) {
    final String processInstanceId = getProcessActorId(piId);
    final Optional<ActorRef> actorOpt = akkaSelector.findActor(getContext(), processInstanceId);

    if (actorOpt.isPresent()) {
      actorOpt.get().forward(msg, getContext());
    }
  }

  private void handleStateObjectChangeMessage(final Object obj) {
    final StateObjectChangeMessage.Request request = (StateObjectChangeMessage.Request) obj;
    forwardToProcessActor(request.getPiId(), request);
  }
}
