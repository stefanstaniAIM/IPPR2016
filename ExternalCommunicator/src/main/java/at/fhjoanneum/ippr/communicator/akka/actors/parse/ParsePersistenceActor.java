package at.fhjoanneum.ippr.communicator.akka.actors.parse;

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
import at.fhjoanneum.ippr.communicator.akka.messages.commands.ConfigRetrievalCommand;
import at.fhjoanneum.ippr.communicator.akka.messages.parse.commands.ParseMessageCreateCommand;
import at.fhjoanneum.ippr.communicator.akka.messages.parse.events.ConfigRetrievedEvent;
import at.fhjoanneum.ippr.communicator.akka.messages.parse.events.ParseMessageCreatedEvent;
import at.fhjoanneum.ippr.communicator.persistence.entities.messageflow.MessageBuilder;
import at.fhjoanneum.ippr.communicator.persistence.entities.messageflow.MessageImpl;
import at.fhjoanneum.ippr.communicator.persistence.objects.basic.inbound.BasicInboundConfiguration;
import at.fhjoanneum.ippr.communicator.persistence.objects.messageflow.Message;
import at.fhjoanneum.ippr.communicator.persistence.objects.messageflow.MessageState;
import at.fhjoanneum.ippr.communicator.repositories.BasicInboundConfigurationRepository;
import at.fhjoanneum.ippr.communicator.repositories.MessageRepository;

@Transactional(isolation = Isolation.READ_COMMITTED)
@Component("ParsePersistenceActor")
@Scope("prototype")
public class ParsePersistenceActor extends AbstractActor {

  private final static Logger LOG = LoggerFactory.getLogger(ParsePersistenceActor.class);

  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private BasicInboundConfigurationRepository basicInboundConfigurationRepository;

  public ParsePersistenceActor() {
    receive(
        ReceiveBuilder.match(ParseMessageCreateCommand.class, this::handleParseMessageCreateCommand)
            .match(ConfigRetrievalCommand.class, this::handleConfigRetrievalCommand)
            .matchAny(o -> LOG.warn("Unhandled message [{}]", o)).build());
  }

  private void handleParseMessageCreateCommand(final ParseMessageCreateCommand cmd) {
    final Message msg = new MessageBuilder().messageState(MessageState.TO_PARSE).build();
    msg.setExternalData(cmd.getData());

    final BasicInboundConfiguration config =
        basicInboundConfigurationRepository.findOne(cmd.getConfigId());
    msg.setInboundConfiguration(config);

    messageRepository.save((MessageImpl) msg);
    final String actorId = getContext().parent().path().name();
    final ActorRef sender = sender();

    LOG.info("Stored new message [{}]", msg);
    sender.tell(new ParseMessageCreatedEvent(actorId, msg.getId()), self());
    stop();
  }

  private void handleConfigRetrievalCommand(final ConfigRetrievalCommand cmd) {
    final Message msg = messageRepository.findOne(cmd.getId());
    sender().tell(
        new ConfigRetrievedEvent(cmd.getId(), msg.getInboundConfiguration(), msg.getExternalData()),
        self());
  }

  private void stop() {
    getContext().stop(self());
  }
}
