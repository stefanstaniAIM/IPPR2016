package at.fhjoanneum.ippr.communicator;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import akka.actor.ActorRef;
import at.fhjoanneum.ippr.communicator.akka.messages.compose.commands.ComposeMessageCreateCommand;

@Component
@Transactional
public class TestRunner implements CommandLineRunner {

  @Autowired
  private ActorRef composeSupervisorActor;

  @Override
  public void run(final String... args) throws Exception {
    composeSupervisorActor.tell(new ComposeMessageCreateCommand("xoxo"), ActorRef.noSender());
  }
}
