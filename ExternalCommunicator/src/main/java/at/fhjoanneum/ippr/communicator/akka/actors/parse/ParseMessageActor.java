package at.fhjoanneum.ippr.communicator.akka.actors.parse;

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
import at.fhjoanneum.ippr.communicator.akka.messages.commands.ConfigRetrievalCommand;
import at.fhjoanneum.ippr.communicator.akka.messages.parse.commands.ParseMessageCommand;
import at.fhjoanneum.ippr.communicator.akka.messages.parse.commands.ParseMessageCreateCommand;

@Transactional(isolation = Isolation.READ_COMMITTED)
@Component("ParseMessageActor")
@Scope("prototype")
public class ParseMessageActor extends AbstractActor {

  private final static Logger LOG = LoggerFactory.getLogger(ParseMessageActor.class);

  @Autowired
  private SpringExtension springExtension;

  public ParseMessageActor() {
    receive(ReceiveBuilder
        .match(ParseMessageCreateCommand.class,
            cmd -> getDBPersistenceActor().tell(cmd, getContext().parent()))
        .match(ParseMessageCommand.class, this::handleParseMessageCommand)
        .matchAny(o -> LOG.warn("Unhandled message [{}]", o)).build());
  }

  private void handleParseMessageCommand(final ParseMessageCommand cmd) {
    getDBPersistenceActor().tell(new ConfigRetrievalCommand(cmd.getId()), self());
  }

  private ActorRef getDBPersistenceActor() {
    return getContext().actorOf(springExtension.props("ParsePersistenceActor"),
        UUID.randomUUID().toString());
  }
}
