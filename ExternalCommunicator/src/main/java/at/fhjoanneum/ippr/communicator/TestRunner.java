package at.fhjoanneum.ippr.communicator;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import akka.actor.ActorSystem;

@Component
@Transactional
public class TestRunner implements CommandLineRunner {

  @Autowired
  private ActorSystem actorSystem;

  @Override
  public void run(final String... args) throws Exception {

  }
}
