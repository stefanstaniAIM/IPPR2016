package at.fhjoanneum.ippr.communicator.akka.actors.parse;

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

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;
import at.fhjoanneum.ippr.communicator.akka.config.SpringExtension;
import at.fhjoanneum.ippr.communicator.akka.messages.events.WorkflowFinishedEvent;
import at.fhjoanneum.ippr.communicator.akka.messages.parse.commands.NotifyProcessEngineCommand;
import at.fhjoanneum.ippr.communicator.akka.messages.parse.commands.ParseMessageCommand;
import at.fhjoanneum.ippr.communicator.akka.messages.parse.commands.ParseMessageCreateCommand;
import at.fhjoanneum.ippr.communicator.akka.messages.parse.events.ParseMessageCreatedEvent;
import at.fhjoanneum.ippr.communicator.akka.messages.parse.events.ParsedMessageEvent;

@Transactional(isolation = Isolation.READ_COMMITTED)
@Component("ParseSupervisorActor")
@Scope("prototype")
public class ParseSupervisorActor extends AbstractActor {

  private final static Logger LOG = LoggerFactory.getLogger(ParseSupervisorActor.class);

  @Autowired
  private SpringExtension springExtension;

  private final Map<String, ActorRef> actors = new HashMap<>();

  public ParseSupervisorActor() {
    receive(
        ReceiveBuilder.match(ParseMessageCreateCommand.class, this::handleParseMessageCreateCommand)
            .match(ParseMessageCreatedEvent.class, this::handleParseMessageCreatedEvent)
            .match(ParsedMessageEvent.class, this::handleParsedMessageEvent)
            .match(WorkflowFinishedEvent.class, this::handleWorkflowFinishedEvent)
            .matchAny(o -> LOG.warn("Unhandled message [{}]", o)).build());
  }

  private void handleParseMessageCreateCommand(final ParseMessageCreateCommand cmd) {
    final String id = UUID.randomUUID().toString();
    final ActorRef actor = getContext().actorOf(springExtension.props("ParseMessageActor"), id);
    actor.tell(cmd, self());
    actors.put(id, actor);
  }

  private void handleParseMessageCreatedEvent(final ParseMessageCreatedEvent evt) {
    final ActorRef actor = actors.get(evt.getActorId());
    actor.tell(new ParseMessageCommand(evt.getId()), self());
  }

  private void handleParsedMessageEvent(final ParsedMessageEvent evt) {
    final ActorRef actor = actors.get(evt.getActorId());
    actor.tell(new NotifyProcessEngineCommand(evt.getId()), self());
  }

  private void handleWorkflowFinishedEvent(final WorkflowFinishedEvent evt) {
    final ActorRef actor = actors.get(evt.getActorId());
    getContext().stop(actor);
    actors.remove(evt.getActorId());
  }
}
