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
import at.fhjoanneum.ippr.communicator.akka.messages.commands.StoreExternalDataCommand;
import at.fhjoanneum.ippr.communicator.akka.messages.compose.commands.ComposeMessageCommand;
import at.fhjoanneum.ippr.communicator.akka.messages.compose.commands.ComposeMessageCreateCommand;
import at.fhjoanneum.ippr.communicator.akka.messages.compose.commands.ConfigRetrievalCommand;
import at.fhjoanneum.ippr.communicator.akka.messages.compose.events.ConfigRetrievedEvent;
import at.fhjoanneum.ippr.communicator.composer.Composer;

@Transactional(isolation = Isolation.READ_COMMITTED)
@Component("ComposeMessageActor")
@Scope("prototype")
public class ComposeMessageActor extends UntypedActor {

  private final static Logger LOG = LoggerFactory.getLogger(ComposeMessageActor.class);

  @Autowired
  private SpringExtension springExtension;

  @Override
  public void onReceive(final Object msg) throws Throwable {
    if (msg instanceof ComposeMessageCreateCommand) {
      handleComposeMessageCreateCommand(msg);
    } else if (msg instanceof ComposeMessageCommand) {
      handleComposeMessageCommand(msg);
    } else if (msg instanceof ConfigRetrievedEvent) {
      handleConfigRetrievedEvent(msg);
    } else {
      LOG.warn("Unhandled message [{}]", msg);
      unhandled(msg);
    }
  }

  private void handleComposeMessageCreateCommand(final Object msg) {
    getDBPersistenceActor().forward(msg, getContext());
  }

  private void handleComposeMessageCommand(final Object msg) {
    final ComposeMessageCommand cmd = (ComposeMessageCommand) msg;

    // TODO get config from cmd
    getDBPersistenceActor().tell(new ConfigRetrievalCommand(cmd.getId(), 1L), getSelf());


  }

  private void handleConfigRetrievedEvent(final Object msg)
      throws InstantiationException, IllegalAccessException, ClassNotFoundException {
    final ConfigRetrievedEvent evt = (ConfigRetrievedEvent) msg;

    final Composer composer =
        getClass().getClassLoader().loadClass(evt.getBasicConfiguration().getComposerClass())
            .asSubclass(Composer.class).newInstance();

    final String composedValue = composer.compose(evt.getTransferId(), evt.getData(),
        evt.getBasicConfiguration().getMessageProtocol(),
        evt.getBasicConfiguration().getDataTypeComposer());

    getDBPersistenceActor().forward(new StoreExternalDataCommand(evt.getId(), composedValue),
        getContext());
  }

  private ActorRef getDBPersistenceActor() {
    return getContext().actorOf(springExtension.props("DBPersistenceActor"),
        UUID.randomUUID().toString());
  }
}
