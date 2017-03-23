package at.fhjoanneum.ippr.communicator.akka.actors.compose;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import at.fhjoanneum.ippr.communicator.akka.config.SpringExtension;
import at.fhjoanneum.ippr.communicator.akka.messages.compose.commands.ComposeMessageCommand;
import at.fhjoanneum.ippr.communicator.akka.messages.compose.commands.ComposeMessageCreateCommand;
import at.fhjoanneum.ippr.communicator.akka.messages.compose.commands.SendMessageCommand;
import at.fhjoanneum.ippr.communicator.akka.messages.compose.events.ComposeMessageCreatedEvent;
import at.fhjoanneum.ippr.communicator.akka.messages.compose.events.ComposedMessageEvent;
import at.fhjoanneum.ippr.communicator.akka.messages.events.WorkflowFinishedEvent;

@Transactional(isolation = Isolation.READ_COMMITTED)
@Component("ComposeSupervisorActor")
@Scope("prototype")
public class ComposeSupervisorActor extends UntypedActor {

  private final static Logger LOG = LoggerFactory.getLogger(ComposeSupervisorActor.class);

  private final Map<String, ActorRef> actors = new HashMap<>();

  @Autowired
  private SpringExtension springExtension;

  @Override
  public void onReceive(final Object msg) throws Throwable {
    if (msg instanceof ComposeMessageCreateCommand) {
      handleComposeMessageCreateCommand(msg);
    } else if (msg instanceof ComposeMessageCreatedEvent) {
      handleComposeMessageCreatedEvent(msg);
    } else if (msg instanceof ComposedMessageEvent) {
      handleComposedMessagenEvent(msg);
    } else if (msg instanceof WorkflowFinishedEvent) {
      handleWorkflowFinishedEvent(msg);
    } else {
      LOG.warn("Unhandled message [{}]", msg);
      unhandled(msg);
    }
  }

  private void handleComposeMessageCreateCommand(final Object msg) {
    final String id = UUID.randomUUID().toString();
    final ActorRef actor = getContext().actorOf(springExtension.props("ComposeMessageActor"), id);
    actor.tell(msg, getSelf());
    actors.put(id, actor);
  }

  private void handleComposeMessageCreatedEvent(final Object msg) {
    final ComposeMessageCreatedEvent evt = (ComposeMessageCreatedEvent) msg;
    final ActorRef actor = actors.get(evt.getActorId());
    actor.tell(new ComposeMessageCommand(evt.getId()), getSelf());
  }

  private void handleComposedMessagenEvent(final Object msg) {
    final ComposedMessageEvent evt = (ComposedMessageEvent) msg;
    final ActorRef actor = actors.get(evt.getActorId());
    actor.tell(new SendMessageCommand(evt.getId()), getSelf());
  }

  private void handleWorkflowFinishedEvent(final Object msg) {
    final WorkflowFinishedEvent evt = (WorkflowFinishedEvent) msg;
    final ActorRef actor = actors.get(evt.getActorId());
    getContext().stop(actor);
    actors.remove(evt.getActorId());
  }
}
