package at.fhjoanneum.ippr.processengine.akka.actors.process;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import akka.actor.UntypedActor;
import at.fhjoanneum.ippr.processengine.akka.messages.process.info.ProcessStateMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.stop.ProcessStopMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.workflow.StateObjectChangeMessage;
import at.fhjoanneum.ippr.processengine.akka.tasks.TaskAllocation;
import at.fhjoanneum.ippr.processengine.akka.tasks.TaskManager;

@Transactional(isolation = Isolation.READ_COMMITTED)
@Component("ProcessActor")
@Scope("prototype")
public class ProcessActor extends UntypedActor {

  private final static Logger LOG = LoggerFactory.getLogger(ProcessActor.class);

  @Autowired
  private TaskManager taskManager;

  private final Long piId;

  public ProcessActor(final Long piId) {
    this.piId = piId;
  }

  @Override
  public void onReceive(final Object obj) throws Throwable {
    if (obj instanceof ProcessStateMessage.Request) {
      handleProcessStateMessage(obj);
    } else if (obj instanceof ProcessStopMessage.Request) {
      handleProcessStopMessage(obj);
    } else if (obj instanceof StateObjectChangeMessage.Request) {
      handleStateObjectChangeMessage(obj);
    } else {
      LOG.warn("Unhandled message: {}", obj);
      unhandled(obj);
    }
  }

  private void handleProcessStateMessage(final Object obj) {
    taskManager.executeTask(TaskAllocation.PROCESS_STATE_TASK, getContext(), obj);
  }

  private void handleProcessStopMessage(final Object obj) {
    taskManager.executeTask(TaskAllocation.PROCESS_STOP_TASK, getContext(), obj,
        result -> getContext().stop(getSelf()));

  }

  private void handleStateObjectChangeMessage(final Object obj) {
    taskManager.executeTask(TaskAllocation.PROCESS_STATE_CHANGE_TASK, getContext(), obj,
        (final Boolean result) -> {
          if (result.booleanValue()) {
            getContext().stop(getSelf());
          }
        });
  }
}
