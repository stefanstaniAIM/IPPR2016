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
import at.fhjoanneum.ippr.persistence.objects.engine.enums.ProcessInstanceState;
import at.fhjoanneum.ippr.processengine.akka.config.SpringExtension;
import at.fhjoanneum.ippr.processengine.akka.messages.analysis.AmountOfActiveProcessesMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.analysis.AmountOfProcessesPerUserMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.ProcessStartMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.check.ProcessCheckMessage;
import scala.concurrent.duration.Duration;

@Service
public class ProcessServiceImpl implements ProcessService {

  private final static Logger LOG = LoggerFactory.getLogger(ProcessServiceImpl.class);

  private final static Timeout TIMEOUT = new Timeout(Duration.create(10, TimeUnit.SECONDS));

  @Autowired
  private ActorSystem actorSystem;

  @Autowired
  private SpringExtension springExtension;

  @Autowired
  private ActorRef processSupervisorActor;

  @Autowired
  private ActorRef userSupervisorActor;

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
        .ask(processCheckActor,
            new ProcessCheckMessage.Request(processStartDTO.getPmId(), subjects), TIMEOUT)
        .toCompletableFuture().thenApply(obj -> (ProcessCheckMessage.Response) obj)
        .whenComplete((msg, exc) -> handleCheckProcessResponse(processStartDTO, future, msg));

    return future;
  }

  private void handleCheckProcessResponse(final ProcessStartDTO processStartDTO,
      final CompletableFuture<ProcessStartedDTO> future,
      final ProcessCheckMessage.Response checkMsg) {
    if (checkMsg.isCorrect()) {
      final List<ProcessStartMessage.UserGroupAssignment> assignments =
          processStartDTO.getAssignments().stream()
              .map(entry -> new ProcessStartMessage.UserGroupAssignment(entry.getSmId(),
                  entry.getUserId(), entry.getGroupId()))
              .collect(Collectors.toList());
      final ProcessStartMessage.Request processStartMessage =
          new ProcessStartMessage.Request(processStartDTO.getPmId(), assignments);

      PatternsCS.ask(processSupervisorActor, processStartMessage, TIMEOUT).toCompletableFuture()
          .thenApply(obj -> (ProcessStartMessage.Response) obj)
          .whenComplete((msg, exc) -> handleProcessStartedResponse(future, msg, exc));
    } else {
      future.complete(new ProcessStartedDTO(null, "Process check returned false"));
    }
  }

  private void handleProcessStartedResponse(final CompletableFuture<ProcessStartedDTO> future,
      final ProcessStartMessage.Response msg, final Throwable exc) {
    if (exc != null) {
      future.complete(new ProcessStartedDTO(null, exc.getCause().getMessage()));
    } else {
      future.complete(new ProcessStartedDTO(msg.getProcessId(), null));
    }
  }

  @Async
  @Override
  public Future<Long> getAmountOfActiveProcesses() {
    final CompletableFuture<Long> future = new CompletableFuture<>();

    final ActorRef analysisActor = getAnalysisActor();

    PatternsCS.ask(analysisActor, new AmountOfActiveProcessesMessage.Request(), TIMEOUT)
        .toCompletableFuture().thenApply(obj -> (AmountOfActiveProcessesMessage.Response) obj)
        .whenComplete((msg, exc) -> future.complete(msg.getAmount()));

    return future;
  }


  @Async
  @Override
  public Future<Long> getAmountOfActiveProcessesPerUser(final Long userId) {
    final CompletableFuture<Long> future = new CompletableFuture<>();

    final ActorRef analysisActor = getAnalysisActor();

    PatternsCS.ask(analysisActor,
        new AmountOfProcessesPerUserMessage.Request(userId, ProcessInstanceState.ACTIVE), TIMEOUT)
        .toCompletableFuture().thenApply(obj -> (AmountOfProcessesPerUserMessage.Response) obj)
        .whenComplete((msg, exc) -> {
          future.complete(msg.getAmount());
        });

    return future;
  }

  private ActorRef getAnalysisActor() {
    final ActorRef analysisActor = actorSystem.actorOf(
        springExtension.props("ProcessAnalysisActor"), "processAnalysisActor-" + UUID.randomUUID());
    return analysisActor;
  }
}
