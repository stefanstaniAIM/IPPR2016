package at.fhjoanneum.ippr.communicator.akka.actors.compose;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.fasterxml.jackson.databind.ObjectMapper;

import akka.actor.UntypedActor;
import at.fhjoanneum.ippr.communicator.akka.messages.commands.StoreExternalDataCommand;
import at.fhjoanneum.ippr.communicator.akka.messages.compose.commands.ComposeMessageCreateCommand;
import at.fhjoanneum.ippr.communicator.akka.messages.compose.events.ComposeMessageCreatedEvent;
import at.fhjoanneum.ippr.communicator.persistence.entities.messageflow.InternalData;
import at.fhjoanneum.ippr.communicator.persistence.entities.messageflow.InternalField;
import at.fhjoanneum.ippr.communicator.persistence.entities.messageflow.InternalObject;
import at.fhjoanneum.ippr.communicator.persistence.entities.messageflow.MessageBuilder;
import at.fhjoanneum.ippr.communicator.persistence.entities.messageflow.MessageImpl;
import at.fhjoanneum.ippr.communicator.persistence.objects.DataType;
import at.fhjoanneum.ippr.communicator.persistence.objects.messageflow.Message;
import at.fhjoanneum.ippr.communicator.repositories.MessageRepository;

@Transactional(isolation = Isolation.READ_COMMITTED)
@Component("DBPersistenceActor")
@Scope("prototype")
public class DBPersistenceActor extends UntypedActor {

  private final static Logger LOG = LoggerFactory.getLogger(DBPersistenceActor.class);

  @Autowired
  private MessageRepository messageRepository;

  @Override
  public void onReceive(final Object msg) throws Throwable {
    if (msg instanceof ComposeMessageCreateCommand) {
      handleComposeMessageCreateCommand(msg);
    } else if (msg instanceof StoreExternalDataCommand) {
      handleStoreExternalDataCommand(msg);
    } else {
      LOG.warn("Unhandled message [{}]", msg);
      unhandled(msg);
    }
  }

  private void handleComposeMessageCreateCommand(final Object obj) throws IOException {
    final ComposeMessageCreateCommand cmd = (ComposeMessageCreateCommand) obj;
    final Message message = new MessageBuilder().transferId(cmd.getTransferId()).build();

    final InternalField internalField =
        new InternalField("string field", DataType.STRING, "test value");
    final Map<String, InternalField> fieldMap = new HashMap<>();
    fieldMap.put("string field", internalField);
    final InternalObject internalObject = new InternalObject("my object", fieldMap);
    final Map<String, InternalObject> objectMap = new HashMap<>();
    objectMap.put("my object", internalObject);
    final InternalData internalData = new InternalData(objectMap);
    final ObjectMapper mapper = new ObjectMapper();
    final String value = mapper.writeValueAsString(internalData);
    LOG.debug("jackson parsing: {}", value);

    message.setInternalData(value);

    mapper.readValue(message.getInternalData(), InternalData.class);


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

    TransactionSynchronizationManager
        .registerSynchronization(new TransactionSynchronizationAdapter() {
          @Override
          public void afterCommit() {
            LOG.info("Updated external data of [{}]", message);

            stop();
          }
        });
  }

  private void stop() {
    getContext().stop(getSelf());
  }
}
