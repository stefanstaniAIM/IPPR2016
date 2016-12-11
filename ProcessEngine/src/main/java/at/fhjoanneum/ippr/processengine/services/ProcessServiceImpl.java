package at.fhjoanneum.ippr.processengine.services;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.PatternsCS;
import at.fhjoanneum.ippr.commons.dto.processengine.ProcessInfoDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.ProcessStartDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.ProcessStartedDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.ProcessStateDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.TaskDTO;
import at.fhjoanneum.ippr.persistence.objects.engine.enums.ProcessInstanceState;
import at.fhjoanneum.ippr.processengine.akka.config.Global;
import at.fhjoanneum.ippr.processengine.akka.config.SpringExtension;
import at.fhjoanneum.ippr.processengine.akka.messages.analysis.AmountOfActiveProcessesMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.analysis.AmountOfProcessesPerUserMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.check.ProcessCheckMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.info.ProcessInfoMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.info.ProcessStateMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.info.TasksOfUserMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.initialize.ActorInitializeMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.initialize.ProcessStartMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.stop.ProcessStopMessage;

@Service
public class ProcessServiceImpl implements ProcessService {

  private final static Logger LOG = LoggerFactory.getLogger(ProcessServiceImpl.class);

  @Autowired
  private ActorSystem actorSystem;

  @Autowired
  private SpringExtension springExtension;

  @Autowired
  private ActorRef processSupervisorActor;

  @Autowired
  private ActorRef userSupervisorActor;

  @Transactional
  @Async
  @Override
  public Future<ProcessStartedDTO> startProcess(final ProcessStartDTO processStartDTO) {
    final CompletableFuture<ProcessStartedDTO> future = new CompletableFuture<>();
    LOG.info("Ask ProcessCheckActor if parameters are correct for PM_ID [{}]",
        processStartDTO.getPmId());

    final ActorRef processCheckActor = actorSystem.actorOf(
        springExtension.props("ProcessCheckActor"), "processCheckActor-" + UUID.randomUUID());

    PatternsCS
        .ask(processCheckActor, new ProcessCheckMessage.Request(processStartDTO.getPmId()),
            Global.TIMEOUT)
        .toCompletableFuture().thenApply(obj -> (ProcessCheckMessage.Response) obj)
        .whenComplete((msg, exc) -> handleCheckProcessResponse(processStartDTO, future, msg));

    return future;
  }

  private void handleCheckProcessResponse(final ProcessStartDTO processStartDTO,
      final CompletableFuture<ProcessStartedDTO> future,
      final ProcessCheckMessage.Response checkMsg) {
    if (checkMsg.isCorrect()) {
      final ProcessStartMessage.Request processStartMessage = new ProcessStartMessage.Request(
          processStartDTO.getPmId(), processStartDTO.getStartUserId());

      PatternsCS.ask(processSupervisorActor, processStartMessage, Global.TIMEOUT)
          .toCompletableFuture().thenApply(obj -> (ProcessStartMessage.Response) obj)
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
      PatternsCS.ask(userSupervisorActor, new ActorInitializeMessage.Request(msg.getProcessId()),
          Global.TIMEOUT).whenComplete((resp, error) -> {
            if (error != null) {
              final String errorText = "Error occured when setting subjects to start state";
              LOG.error(errorText);
              future.complete(new ProcessStartedDTO(null, errorText));
            } else {
              LOG.info("Process instance was sucessfully created PI_ID [{}]", msg.getProcessId());
              future.complete(new ProcessStartedDTO(msg.getProcessId(), null));
            }
          });
    }
  }

  @Async
  @Override
  public Future<Long> getAmountOfActiveProcesses() {
    final CompletableFuture<Long> future = new CompletableFuture<>();

    final ActorRef analysisActor = getAnalysisActor();

    PatternsCS.ask(analysisActor, new AmountOfActiveProcessesMessage.Request(), Global.TIMEOUT)
        .toCompletableFuture().thenApply(obj -> (AmountOfActiveProcessesMessage.Response) obj)
        .whenComplete((msg, exc) -> future.complete(msg.getAmount()));

    return future;
  }


  @Async
  @Override
  public Future<Long> getAmountOfActiveProcessesPerUser(final Long userId) {
    final CompletableFuture<Long> future = new CompletableFuture<>();

    final ActorRef analysisActor = getAnalysisActor();

    PatternsCS
        .ask(analysisActor,
            new AmountOfProcessesPerUserMessage.Request(userId, ProcessInstanceState.ACTIVE),
            Global.TIMEOUT)
        .toCompletableFuture().thenApply(obj -> (AmountOfProcessesPerUserMessage.Response) obj)
        .whenComplete((msg, exc) -> future.complete(msg.getAmount()));

    return future;
  }

  private ActorRef getAnalysisActor() {
    final ActorRef analysisActor = actorSystem.actorOf(
        springExtension.props("ProcessAnalysisActor"), "ProcessAnalysisActor-" + UUID.randomUUID());
    return analysisActor;
  }

  @Override
  public Future<ProcessStateDTO> getStateOfProcessInstance(final Long piId) {
    final CompletableFuture<ProcessStateDTO> future = new CompletableFuture<>();

    PatternsCS.ask(processSupervisorActor, new ProcessStateMessage.Request(piId), Global.TIMEOUT)
        .toCompletableFuture().whenComplete((msg, exc) -> {
          if (exc == null) {
            future.complete(((ProcessStateMessage.Response) msg).getProcessStateDTO());
          } else {
            future.completeExceptionally(exc);
          }
        });

    return future;
  }

  @Async
  @Override
  public Future<List<ProcessInfoDTO>> getProcessesInfoOfState(final String state, final int page,
      final int size) {
    final CompletableFuture<List<ProcessInfoDTO>> future = new CompletableFuture<>();

    PatternsCS
        .ask(processSupervisorActor,
            new ProcessInfoMessage.Request(state.toUpperCase(), page, size), Global.TIMEOUT)
        .toCompletableFuture().whenComplete((msg, exc) -> {
          if (exc == null) {
            future.complete(((ProcessInfoMessage.Response) msg).getProcesses());
          } else {
            future.completeExceptionally(exc);
          }
        });

    return future;
  }

  @Async
  @Override
  public Future<List<ProcessInfoDTO>> getProcessesInfoOfUserAndState(final Long user,
      final String state, final int page, final int size) {
    final CompletableFuture<List<ProcessInfoDTO>> future = new CompletableFuture<>();

    PatternsCS
        .ask(processSupervisorActor,
            new ProcessInfoMessage.Request(user, state.toUpperCase(), page, size), Global.TIMEOUT)
        .toCompletableFuture().whenComplete((msg, exc) -> {
          if (exc == null) {
            future.complete(((ProcessInfoMessage.Response) msg).getProcesses());
          } else {
            future.completeExceptionally(exc);
          }
        });

    return future;
  }

  @Transactional
  @Async
  @Override
  public Future<ProcessInfoDTO> stopProcess(final Long piId) {
    final CompletableFuture<ProcessInfoDTO> future = new CompletableFuture<>();

    final ProcessStopMessage.Request request = new ProcessStopMessage.Request(piId);

    PatternsCS.ask(processSupervisorActor, request, Global.TIMEOUT).toCompletableFuture()
        .whenComplete((msg, exc) -> {
          if (exc == null) {
            userSupervisorActor.tell(request, null);
            future.complete(((ProcessStopMessage.Response) msg).getProcess());
          } else {
            future.completeExceptionally(exc);
          }
        });

    return future;
  }

  @Async
  @Override
  public Future<List<TaskDTO>> getTasksOfUser(final Long userId) {
    final CompletableFuture<List<TaskDTO>> future = new CompletableFuture<>();

    final TasksOfUserMessage.Request request = new TasksOfUserMessage.Request(userId);

    PatternsCS.ask(userSupervisorActor, request, Global.TIMEOUT).toCompletableFuture()
        .whenComplete((msg, exc) -> {
          if (exc == null) {
            future.complete(((TasksOfUserMessage.Response) msg).getTasks());
          } else {
            future.completeExceptionally(exc);
          }
        });

    return future;
  }
}
