package at.fhjoanneum.ippr.processengine.services;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.PatternsCS;
import akka.util.Timeout;
import at.fhjoanneum.ippr.commons.dto.processengine.ProcessStartDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.ProcessStartedDTO;
import at.fhjoanneum.ippr.processengine.akka.config.SpringExtension;
import at.fhjoanneum.ippr.processengine.akka.messages.process.ProcessStartMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.ProcessStartedMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.check.ProcessCheckResponseMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.check.ProcessToCheckMessage;
import scala.concurrent.duration.Duration;

@Service
public class ProcessServiceImpl implements ProcessService {

  private final static Logger LOG = LoggerFactory.getLogger(ProcessServiceImpl.class);

  private final static Timeout TIMEOUT = new Timeout(Duration.create(10, TimeUnit.SECONDS));

  @Autowired
  private ActorSystem actorSystem;

  @Autowired
  private SpringExtension springExtension;

  private final ActorRef processSupervisorActor;

  @Autowired
  public ProcessServiceImpl(final ActorSystem actorSystem, final SpringExtension springExtension) {
    processSupervisorActor = actorSystem.actorOf(springExtension.props("ProcessSupervisorActor"),
        "processSupervisorActor");
  }

  @Async
  @Override
  public Future<ProcessStartedDTO> startProcess(final ProcessStartDTO processStartDTO) {
    final CompletableFuture<ProcessStartedDTO> future = new CompletableFuture<>();
    LOG.info("Ask ProcessCheckActor if parameters are correct for PM_ID [{}]",
        processStartDTO.getPmId());

    final ActorRef processCheckActor = actorSystem.actorOf(
        springExtension.props("ProcessCheckActor"), "processCheckActor-" + UUID.randomUUID());

    final List<Long> subjects = processStartDTO.getAssignments().stream()
        .map(assignment -> assignment.getSmId()).collect(Collectors.toList());

    PatternsCS
        .ask(processCheckActor, new ProcessToCheckMessage(processStartDTO.getPmId(), subjects),
            TIMEOUT)
        .toCompletableFuture().thenApply(obj -> (ProcessCheckResponseMessage) obj)
        .whenComplete((msg, exc) -> handleCheckProcessResponse(processStartDTO, future, msg));

    return future;
  }

  private void handleCheckProcessResponse(final ProcessStartDTO processStartDTO,
      final CompletableFuture<ProcessStartedDTO> future,
      final ProcessCheckResponseMessage checkMsg) {
    if (checkMsg.isCorrect()) {
      final List<ProcessStartMessage.UserGroupAssignment> assignments =
          processStartDTO.getAssignments().stream()
              .map(entry -> new ProcessStartMessage.UserGroupAssignment(entry.getSmId(),
                  entry.getUserId(), entry.getGroupId()))
              .collect(Collectors.toList());
      final ProcessStartMessage processStartMessage =
          new ProcessStartMessage(processStartDTO.getPmId(), assignments);

      PatternsCS.ask(processSupervisorActor, processStartMessage, TIMEOUT).toCompletableFuture()
          .thenApply(obj -> (ProcessStartedMessage) obj)
          .whenComplete((msg, exc) -> handleProcessStartedResponse(future, msg, exc));
    } else {
      future.complete(new ProcessStartedDTO(null, "Process check returned false"));
    }
  }

  private void handleProcessStartedResponse(final CompletableFuture<ProcessStartedDTO> future,
      final ProcessStartedMessage msg, final Throwable exc) {
    if (exc != null) {
      future.complete(new ProcessStartedDTO(null, exc.getCause().getMessage()));
    } else {
      future.complete(new ProcessStartedDTO(msg.getProcessId(), null));
    }
  }
}
