package at.fhjoanneum.ippr.communicator.akka.actors.parse;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;
import at.fhjoanneum.ippr.communicator.akka.messages.commands.ConfigRetrievalCommand;
import at.fhjoanneum.ippr.communicator.akka.messages.commands.UpdateMessageStateCommand;
import at.fhjoanneum.ippr.communicator.akka.messages.events.WorkflowFinishedEvent;
import at.fhjoanneum.ippr.communicator.akka.messages.parse.commands.NotifyProcessEngineCommand;
import at.fhjoanneum.ippr.communicator.akka.messages.parse.commands.ParseMessageCreateCommand;
import at.fhjoanneum.ippr.communicator.akka.messages.parse.commands.StoreInternalDataCommand;
import at.fhjoanneum.ippr.communicator.akka.messages.parse.events.ConfigRetrievedEvent;
import at.fhjoanneum.ippr.communicator.akka.messages.parse.events.NotifyConfigRetrievedEvent;
import at.fhjoanneum.ippr.communicator.akka.messages.parse.events.ParseMessageCreatedEvent;
import at.fhjoanneum.ippr.communicator.akka.messages.parse.events.ParsedMessageEvent;
import at.fhjoanneum.ippr.communicator.persistence.entities.messageflow.MessageBuilder;
import at.fhjoanneum.ippr.communicator.persistence.entities.messageflow.MessageImpl;
import at.fhjoanneum.ippr.communicator.persistence.objects.basic.inbound.BasicInboundConfiguration;
import at.fhjoanneum.ippr.communicator.persistence.objects.messageflow.Message;
import at.fhjoanneum.ippr.communicator.persistence.objects.messageflow.MessageState;
import at.fhjoanneum.ippr.communicator.repositories.BasicInboundConfigurationRepository;
import at.fhjoanneum.ippr.communicator.repositories.MessageRepository;
import at.fhjoanneum.ippr.communicator.utils.InternalDataUtils;

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
            .match(StoreInternalDataCommand.class, this::handleStoreInternalDataCommand)
            .match(NotifyProcessEngineCommand.class, this::handleNotifyProcessEngineCommand)
            .match(UpdateMessageStateCommand.class, this::handleUpdateMessageStateCommand)
            .matchAny(o -> LOG.warn("Unhandled message [{}]", o)).build());
  }

  private void handleParseMessageCreateCommand(final ParseMessageCreateCommand cmd) {
    final Message msg = new MessageBuilder().messageState(MessageState.TO_PARSE).build();
    msg.setExternalData(cmd.getData());

    final BasicInboundConfiguration config =
        basicInboundConfigurationRepository.findOne(cmd.getConfigId());
    msg.setInboundConfiguration(config);

    messageRepository.save((MessageImpl) msg);
    final ActorRef sender = sender();
    final String actorId = getContext().parent().path().name();

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

  private void handleStoreInternalDataCommand(final StoreInternalDataCommand cmd)
      throws JsonProcessingException {
    final Message msg = messageRepository.findOne(cmd.getId());
    msg.setInternalData(InternalDataUtils.convertInternalDataToJson(cmd.getData()));
    msg.setMessageState(MessageState.PARSED);
    LOG.debug("Updated message [{}]", msg);

    messageRepository.save((MessageImpl) msg);
    final String actorId = getContext().parent().path().name();
    sender().tell(new ParsedMessageEvent(actorId, cmd.getId()), self());
    stop();
  }

  private void handleNotifyProcessEngineCommand(final NotifyProcessEngineCommand cmd)
      throws JsonParseException, JsonMappingException, IOException {
    final Message msg = messageRepository.findOne(cmd.getId());
    sender().tell(new NotifyConfigRetrievedEvent(cmd.getId(),
        InternalDataUtils.convertJsonToInternalData(msg.getInternalData())), self());
  }

  private void handleUpdateMessageStateCommand(final UpdateMessageStateCommand cmd) {
    final Message message = messageRepository.findOne(cmd.getId());
    message.setMessageState(cmd.getMessageState());
    messageRepository.save((MessageImpl) message);

    final String actorId = getContext().parent().path().name();
    sender().tell(new WorkflowFinishedEvent(actorId), self());
    stop();
  }

  private void stop() {
    getContext().stop(self());
  }
}
