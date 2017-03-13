package at.fhjoanneum.ippr.communicator.akka.actors.compose;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonObject;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import at.fhjoanneum.ippr.communicator.akka.config.SpringExtension;
import at.fhjoanneum.ippr.communicator.akka.messages.commands.StoreExternalDataCommand;
import at.fhjoanneum.ippr.communicator.akka.messages.compose.commands.ComposeMessageCommand;
import at.fhjoanneum.ippr.communicator.akka.messages.compose.commands.ComposeMessageCreateCommand;

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
    final JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("test", "new json :)");
    LOG.debug(jsonObject.toString());
    getDBPersistenceActor()
        .forward(new StoreExternalDataCommand(cmd.getId(), jsonObject.toString()), getContext());

  }

  private ActorRef getDBPersistenceActor() {
    return getContext().actorOf(springExtension.props("DBPersistenceActor"),
        UUID.randomUUID().toString());
  }
}
