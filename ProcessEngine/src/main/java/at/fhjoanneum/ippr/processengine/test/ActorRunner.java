package at.fhjoanneum.ippr.processengine.test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.PatternsCS;
import akka.util.Timeout;
import at.fhjoanneum.ippr.processengine.akka.config.AkkaApplicationContext;
import scala.concurrent.duration.Duration;

// @Component
public class ActorRunner implements CommandLineRunner {

  private final static Logger LOG = LoggerFactory.getLogger(ActorRunner.class);

  @Autowired
  private AkkaApplicationContext applicationContext;

  @Override
  public void run(final String... args) throws Exception {
    LOG.debug("Start the actor testing");

    final ActorSystem system = applicationContext.getActorSystem();
    final ActorRef a =
        system.actorOf(Props.create(ProcessSupervisorActor.class), "processSupervisor");

    final Timeout t = new Timeout(Duration.create(60, TimeUnit.SECONDS));

    final CompletableFuture<Object> response2 = PatternsCS.ask(a, "AAAAA", t).toCompletableFuture();
    response2.thenAccept(object -> {
      LOG.debug("AAAAA yes received: {}", object);
    });


    final CompletableFuture<Object> response3 = PatternsCS.ask(a, "BBBBB", t).toCompletableFuture();
    response3.thenAccept(object -> {
      LOG.debug("BBBBB yes received {}", object);
    });


    final CompletableFuture<Object> response4 = PatternsCS.ask(a, "CCCCC", t).toCompletableFuture();
    response3.thenAccept(object -> {
      LOG.debug("CCCCC yes received {}", object);
    });

    response2.exceptionally(new Function<Throwable, Object>() {

      @Override
      public Object apply(final Throwable t) {
        LOG.error(t.getMessage());
        return null;
      }
    });
  }

}
