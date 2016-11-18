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
import akka.pattern.PatternsCS;
import akka.util.Timeout;
import at.fhjoanneum.ippr.processengine.akka.config.SpringExtension;
import at.fhjoanneum.ippr.processengine.akka.messages.process.ProcessStartMessage;
import scala.concurrent.duration.Duration;

@Service
public class ProcessServiceImpl implements ProcessService {

  private final static Logger LOG = LoggerFactory.getLogger(ProcessServiceImpl.class);

  private final static Timeout TIMEOUT = new Timeout(Duration.create(10, TimeUnit.SECONDS));


  private final ActorRef processSupervisorActor;

  @Autowired
  public ProcessServiceImpl(final ActorSystem actorSystem, final SpringExtension springExtension) {
    processSupervisorActor = actorSystem.actorOf(springExtension.props("ProcessSupervisorActor"),
        "processSupervisorActor");
  }

  @Async
  @Override
  public Future<Object> startProcess(final Long processId) {
    final CompletableFuture<Object> response =
        PatternsCS.ask(processSupervisorActor, new ProcessStartMessage(processId), TIMEOUT)
            .toCompletableFuture();

    return response;
  }
}
