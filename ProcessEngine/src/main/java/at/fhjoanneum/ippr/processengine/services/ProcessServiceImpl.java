package at.fhjoanneum.ippr.processengine.services;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.PatternsCS;
import akka.util.Timeout;
import at.fhjoanneum.ippr.processengine.akka.config.AkkaApplicationContext;
import at.fhjoanneum.ippr.processengine.akka.messages.process.ProcessStartMessage;
import at.fhjoanneum.ippr.processengine.test.ProcessSupervisorActor;
import scala.concurrent.duration.Duration;

@Service
public class ProcessServiceImpl implements ProcessService {

  private final static Logger LOG = LoggerFactory.getLogger(ProcessServiceImpl.class);

  private final static Timeout TIMEOUT = new Timeout(Duration.create(10, TimeUnit.SECONDS));

  private final ActorSystem system;
  private final ActorRef processSupervisorActor;

  @Autowired
  public ProcessServiceImpl(final AkkaApplicationContext applicationContext) {
    system = applicationContext.getActorSystem();
    processSupervisorActor =
        system.actorOf(Props.create(ProcessSupervisorActor.class), "ProcessSupervisorActor");
  }

  @Async
  @Override
  public Future<Object> startProcess(final Long processId) {
    LOG.debug("asynchron");
    final CompletableFuture<Object> response =
        PatternsCS.ask(processSupervisorActor, new ProcessStartMessage(processId), TIMEOUT)
            .toCompletableFuture();

    return response;
  }
}
