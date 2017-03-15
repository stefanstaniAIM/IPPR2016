package at.fhjoanneum.ippr.communicator.akka.actors.compose;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import at.fhjoanneum.ippr.communicator.akka.messages.commands.StoreExternalDataCommand;
import at.fhjoanneum.ippr.communicator.akka.messages.compose.commands.ComposeMessageCreateCommand;
import at.fhjoanneum.ippr.communicator.akka.messages.compose.commands.ConfigRetrievalCommand;
import at.fhjoanneum.ippr.communicator.akka.messages.compose.commands.SendConfigRetrieveCommand;
import at.fhjoanneum.ippr.communicator.akka.messages.compose.events.ComposeMessageCreatedEvent;
import at.fhjoanneum.ippr.communicator.akka.messages.compose.events.ComposedMessageEvent;
import at.fhjoanneum.ippr.communicator.akka.messages.compose.events.ConfigRetrievedEvent;
import at.fhjoanneum.ippr.communicator.akka.messages.compose.events.SendConfigRetrievedEvent;
import at.fhjoanneum.ippr.communicator.persistence.entities.basic.AbstractBasicOutboundConfiguration;
import at.fhjoanneum.ippr.communicator.persistence.entities.messageflow.MessageBuilder;
import at.fhjoanneum.ippr.communicator.persistence.entities.messageflow.MessageImpl;
import at.fhjoanneum.ippr.communicator.persistence.objects.basic.RestOutboundConfiguration;
import at.fhjoanneum.ippr.communicator.persistence.objects.internal.InternalData;
import at.fhjoanneum.ippr.communicator.persistence.objects.messageflow.Message;
import at.fhjoanneum.ippr.communicator.repositories.BasicConfigurationRepository;
import at.fhjoanneum.ippr.communicator.repositories.MessageRepository;

@Transactional(isolation = Isolation.READ_COMMITTED)
@Component("ComposePersistenceActor")
@Scope("prototype")
public class ComposePersistenceActor extends UntypedActor {

  private final static Logger LOG = LoggerFactory.getLogger(ComposePersistenceActor.class);

  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private BasicConfigurationRepository basicConfigurationRepository;

  @Override
  public void onReceive(final Object msg) throws Throwable {
    if (msg instanceof ComposeMessageCreateCommand) {
      handleComposeMessageCreateCommand(msg);
    } else if (msg instanceof StoreExternalDataCommand) {
      handleStoreExternalDataCommand(msg);
    } else if (msg instanceof ConfigRetrievalCommand) {
      handleConfigRetrievalCommand(msg);
    } else if (msg instanceof SendConfigRetrieveCommand) {
      handleSendConfigRetrieveCommand(msg);
    } else {
      LOG.warn("Unhandled message [{}]", msg);
      unhandled(msg);
    }
  }

  private void handleComposeMessageCreateCommand(final Object obj) throws IOException {
    final ComposeMessageCreateCommand cmd = (ComposeMessageCreateCommand) obj;
    final Message message = new MessageBuilder().transferId(cmd.getTransferId()).build();

    final ObjectMapper mapper = new ObjectMapper();
    final String value = mapper.writeValueAsString(cmd.getData());
    message.setInternalData(value);

    messageRepository.save((MessageImpl) message);
    final String actorId = getContext().parent().path().name();
    TransactionSynchronizationManager
        .registerSynchronization(new TransactionSynchronizationAdapter() {
          @Override
          public void afterCommit() {
            LOG.info("Saved new [{}]", message);
            getSender().tell(new ComposeMessageCreatedEvent(actorId, message.getId()), getSelf());
            stop();
          }
        });
  }

  private void handleStoreExternalDataCommand(final Object obj) {
    final StoreExternalDataCommand cmd = (StoreExternalDataCommand) obj;
    final Message message = messageRepository.findOne(cmd.getId());
    message.setExternalData(cmd.getData());
    messageRepository.save((MessageImpl) message);
    final String actorId = getContext().parent().path().name();

    final ActorRef sender = getSender();
    TransactionSynchronizationManager
        .registerSynchronization(new TransactionSynchronizationAdapter() {
          @Override
          public void afterCommit() {
            LOG.info("Updated external data of [{}]", message);
            sender.tell(new ComposedMessageEvent(actorId, message.getId()), getSelf());
            stop();
          }
        });
  }

  private void handleConfigRetrievalCommand(final Object obj)
      throws JsonParseException, JsonMappingException, IOException {
    final ConfigRetrievalCommand cmd = (ConfigRetrievalCommand) obj;

    final AbstractBasicOutboundConfiguration config =
        basicConfigurationRepository.findOne(cmd.getConfigId());
    LOG.debug("Retrieved config [{}]", config);

    final Message msg = messageRepository.findOne(cmd.getId());
    final ObjectMapper mapper = new ObjectMapper();
    final InternalData data = mapper.readValue(msg.getInternalData(), InternalData.class);

    getContext().parent()
        .tell(new ConfigRetrievedEvent(cmd.getId(), msg.getTransferId(), data, config), getSelf());
  }

  private void handleSendConfigRetrieveCommand(final Object obj) {
    final SendConfigRetrieveCommand cmd = (SendConfigRetrieveCommand) obj;

    final AbstractBasicOutboundConfiguration config =
        basicConfigurationRepository.findOne(cmd.getConfigId());
    LOG.debug("Retrieved config [{}]", config);

    final Message msg = messageRepository.findOne(cmd.getId());

    String endpoint = null;
    if (config instanceof RestOutboundConfiguration) {
      final RestOutboundConfiguration restConfig = (RestOutboundConfiguration) config;
      endpoint = restConfig.getEndpoint();
    }

    getContext().parent().tell(new SendConfigRetrievedEvent(cmd.getId(), config.getSendPlugin(),
        endpoint, msg.getExternalData()), getSelf());
  }

  private void stop() {
    getContext().stop(getSelf());
  }
}
