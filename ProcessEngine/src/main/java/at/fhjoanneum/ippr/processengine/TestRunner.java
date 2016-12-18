package at.fhjoanneum.ippr.processengine;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import at.fhjoanneum.ippr.processengine.parser.db.DbTimestampParser;
import scala.concurrent.duration.Duration;

@Component
public class TestRunner implements CommandLineRunner {

  private final static Logger LOG = LoggerFactory.getLogger(TestRunner.class);

  @Autowired
  private ActorSystem actorSystem;

  @Autowired
  private ActorRef userSupervisorActor;

  @Autowired
  private DbTimestampParser dbTimestampParser;

  @Override
  public void run(final String... args) throws Exception {
    LOG.debug(
        "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

    actorSystem.scheduler().scheduleOnce(Duration.create(50, TimeUnit.MILLISECONDS),
        userSupervisorActor, "foo", actorSystem.dispatcher(), null);

    final LocalDateTime dateTime = LocalDateTime.now();
    System.out.println(dbTimestampParser.parse(dateTime));

  }

}
