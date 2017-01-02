package at.fhjoanneum.ippr.processengine.akka.tasks;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import akka.actor.ActorRef;
import akka.actor.UntypedActorContext;
import at.fhjoanneum.ippr.processengine.akka.config.SpringExtension;

@Component
public class TaskManager {

  @Autowired
  private SpringExtension springExtension;

  public void executeTask(final TaskAllocation task, final UntypedActorContext context,
      final Object msg) {
    final ActorRef taskActor =
        context.actorOf(springExtension.props(task.getActorName()), getTaskId());
    taskActor.forward(msg, context);
  }

  public <T> void executeTask(final TaskAllocation task, final UntypedActorContext context,
      final Object msg, final TaskCallback<T> callback) {
    final ActorRef taskActor =
        context.actorOf(springExtension.props(task.getActorName(), callback), getTaskId());
    taskActor.forward(msg, context);
  }

  private synchronized String getTaskId() {
    return "Task-" + String.valueOf(UUID.randomUUID());
  }
}
