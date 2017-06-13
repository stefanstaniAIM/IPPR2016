package at.fhjoanneum.ippr.processengine.startup;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import akka.actor.ActorRef;
import akka.pattern.PatternsCS;
import at.fhjoanneum.ippr.persistence.objects.engine.enums.ProcessInstanceState;
import at.fhjoanneum.ippr.persistence.objects.engine.process.ProcessInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.state.SubjectState;
import at.fhjoanneum.ippr.persistence.objects.engine.subject.Subject;
import at.fhjoanneum.ippr.processengine.akka.config.Global;
import at.fhjoanneum.ippr.processengine.akka.messages.process.timeout.TimeoutScheduleStartMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.wakeup.ProcessWakeUpMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.wakeup.UserActorWakeUpMessage;
import at.fhjoanneum.ippr.processengine.repositories.ProcessInstanceRepository;
import at.fhjoanneum.ippr.processengine.repositories.SubjectStateRepository;

@Service
public class StartUpRunner implements ApplicationRunner {

  private final static Logger LOG = LoggerFactory.getLogger(StartUpRunner.class);

  @Autowired
  private ProcessInstanceRepository processInstanceRepository;

  @Autowired
  private SubjectStateRepository subjectStateRepository;

  @Autowired
  private ActorRef processSupervisorActor;

  @Autowired
  private ActorRef userSupervisorActor;

  final List<CompletableFuture<Object>> futures = Lists.newArrayList();

  @Async
  @Transactional
  @Override
  public void run(final ApplicationArguments args) throws Exception {
    LOG.info(
        "##################################################################################################################");
    LOG.info("Start up of already running processes and users");

    final List<ProcessInstance> processes = Lists.newArrayList(
        processInstanceRepository.getProcessesWithState(ProcessInstanceState.ACTIVE.name()));

    processes.stream()
        .map(process -> PatternsCS.ask(processSupervisorActor,
            new ProcessWakeUpMessage.Request(process.getPiId()), Global.TIMEOUT)
            .toCompletableFuture())
        .forEachOrdered(response -> {
          futures.add(response);
          handleProcessResponse(response);
        });

    final Set<Subject> subjects =
        processes.stream().map(ProcessInstance::getSubjects).flatMap(List::stream)
            .filter(subject -> subject.getUser() != null).collect(Collectors.toSet());

    subjects.stream()
        .map(subject -> PatternsCS.ask(userSupervisorActor,
            new UserActorWakeUpMessage.Request(subject.getUser()), Global.TIMEOUT)
            .toCompletableFuture())
        .forEachOrdered(response -> {
          futures.add(response);
          handleUserResponse(response);
        });

    CompletableFuture.allOf(Iterables.toArray(futures, CompletableFuture.class)).thenRun(() -> {
      final List<SubjectState> subjectStatesWithTimeout =
          subjectStateRepository.getSubjectStatesWithTimeout();
      subjectStatesWithTimeout.stream().forEach(this::notifyTimeoutScheduler);
    });
  }



  private void handleProcessResponse(final CompletableFuture<Object> future) {
    future.whenComplete((resp, exc) -> {
      if (exc != null) {
        LOG.error("Could not wake up process due to: {}", exc.getCause().getMessage());
      } else {
        LOG.info("Process PI_ID [{}] is waked up",
            ((ProcessWakeUpMessage.Response) resp).getPiId());
      }
    });
  }

  private void handleUserResponse(final CompletableFuture<Object> future) {
    future.whenComplete((resp, exc) -> {
      if (exc != null) {
        LOG.error("Could not wake up user due to: {}", exc.getCause().getMessage());
      } else {
        LOG.info("User [{}] is waked up", ((UserActorWakeUpMessage.Request) resp).getUserId());
      }
    });
  }

  private void notifyTimeoutScheduler(final SubjectState subjectState) {
    LOG.debug("Notify timeout scheduler for [{}]", subjectState);
    userSupervisorActor.tell(new TimeoutScheduleStartMessage(subjectState.getSubject().getUser(),
        subjectState.getSsId(), subjectState.getTimeoutActor()), ActorRef.noSender());
  }
}
