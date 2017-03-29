package at.fhjoanneum.ippr.communicator.akka.actors.compose;

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
import at.fhjoanneum.ippr.communicator.akka.messages.commands.ConfigRetrievalCommand;
import at.fhjoanneum.ippr.communicator.akka.messages.commands.UpdateMessageStateCommand;
import at.fhjoanneum.ippr.communicator.akka.messages.compose.commands.ComposeMessageCommand;
import at.fhjoanneum.ippr.communicator.akka.messages.compose.commands.ComposeMessageCreateCommand;
import at.fhjoanneum.ippr.communicator.akka.messages.compose.commands.SendConfigRetrieveCommand;
import at.fhjoanneum.ippr.communicator.akka.messages.compose.commands.SendMessageCommand;
import at.fhjoanneum.ippr.communicator.akka.messages.compose.commands.StoreExternalDataCommand;
import at.fhjoanneum.ippr.communicator.akka.messages.compose.events.ConfigRetrievedEvent;
import at.fhjoanneum.ippr.communicator.akka.messages.compose.events.SendConfigRetrievedEvent;
import at.fhjoanneum.ippr.communicator.composer.Composer;
import at.fhjoanneum.ippr.communicator.feign.ProcessEngineClient;
import at.fhjoanneum.ippr.communicator.persistence.objects.messageflow.MessageState;
import at.fhjoanneum.ippr.communicator.plugins.send.SendPlugin;

@Transactional(isolation = Isolation.READ_COMMITTED)
@Component("ComposeMessageActor")
@Scope("prototype")
public class ComposeMessageActor extends UntypedActor {

  private final static Logger LOG = LoggerFactory.getLogger(ComposeMessageActor.class);

  @Autowired
  private SpringExtension springExtension;

  @Autowired
  private ProcessEngineClient processEngineClient;

  @Override
  public void onReceive(final Object msg) throws Throwable {
    if (msg instanceof ComposeMessageCreateCommand) {
      handleComposeMessageCreateCommand(msg);
    } else if (msg instanceof ComposeMessageCommand) {
      handleComposeMessageCommand(msg);
    } else if (msg instanceof ConfigRetrievedEvent) {
      handleConfigRetrievedEvent(msg);
    } else if (msg instanceof SendMessageCommand) {
      handleSendMessageCommand(msg);
    } else if (msg instanceof SendConfigRetrievedEvent) {
      handleSendConfigRetrievedEvent(msg);
    } else {
      LOG.warn("Unhandled message [{}]", msg);
      unhandled(msg);
    }
  }

  private void handleComposeMessageCreateCommand(final Object msg) {
    getDBPersistenceActor().tell(msg, getContext().parent());
  }

  private void handleComposeMessageCommand(final Object msg) {
    final ComposeMessageCommand cmd = (ComposeMessageCommand) msg;
    getDBPersistenceActor().tell(new ConfigRetrievalCommand(cmd.getId()), getSelf());
  }

  private void handleConfigRetrievedEvent(final Object msg)
      throws InstantiationException, IllegalAccessException, ClassNotFoundException {
    final ConfigRetrievedEvent evt = (ConfigRetrievedEvent) msg;

    final Composer composer =
        getClass().getClassLoader().loadClass(evt.getBasicConfiguration().getComposerClass())
            .asSubclass(Composer.class).newInstance();

    final String composedValue = composer.compose(evt.getTransferId(), evt.getData(),
        evt.getBasicConfiguration().getMessageProtocol(),
        evt.getBasicConfiguration().getDataTypeComposer(),
        evt.getBasicConfiguration().getConfiguration());

    getDBPersistenceActor().tell(new StoreExternalDataCommand(evt.getId(), composedValue),
        getContext().parent());
  }

  private void handleSendMessageCommand(final Object msg) {
    final SendMessageCommand cmd = (SendMessageCommand) msg;
    getDBPersistenceActor().tell(new SendConfigRetrieveCommand(cmd.getId()), getSelf());
  }

  private void handleSendConfigRetrievedEvent(final Object msg)
      throws InstantiationException, IllegalAccessException, ClassNotFoundException {
    final SendConfigRetrievedEvent evt = (SendConfigRetrievedEvent) msg;

    final SendPlugin plugin = getClass().getClassLoader().loadClass(evt.getSendPlugin())
        .asSubclass(SendPlugin.class).newInstance();

    final boolean sent = plugin.send(evt.getBody(), evt.getConfiguration());
    if (sent) {
      processEngineClient.markAsSent(evt.getTransferId());
      getDBPersistenceActor().tell(new UpdateMessageStateCommand(evt.getId(), MessageState.SENT),
          getContext().parent());
    }
  }

  private ActorRef getDBPersistenceActor() {
    return getContext().actorOf(springExtension.props("ComposePersistenceActor"),
        UUID.randomUUID().toString());
  }
}
