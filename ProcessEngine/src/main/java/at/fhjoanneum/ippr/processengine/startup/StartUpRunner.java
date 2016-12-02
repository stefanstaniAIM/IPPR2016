package at.fhjoanneum.ippr.processengine.startup;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import akka.actor.ActorRef;
import akka.pattern.PatternsCS;
import at.fhjoanneum.ippr.persistence.objects.engine.enums.ProcessInstanceState;
import at.fhjoanneum.ippr.persistence.objects.engine.process.ProcessInstance;
import at.fhjoanneum.ippr.processengine.akka.config.Global;
import at.fhjoanneum.ippr.processengine.akka.messages.process.ProcessWakeUpMessage;
import at.fhjoanneum.ippr.processengine.repositories.ProcessInstanceRepository;

@Service
public class StartUpRunner implements CommandLineRunner {

  private final static Logger LOG = LoggerFactory.getLogger(StartUpRunner.class);

  @Autowired
  private ProcessInstanceRepository processInstanceRepository;

  @Autowired
  private ActorRef processSupervisorActor;

  @Transactional
  @Override
  public void run(final String... args) throws Exception {
    LOG.info(
        "##################################################################################################################");
    LOG.info("Start up of already running processes and users");

    final List<ProcessInstance> processes = Lists.newArrayList(
        processInstanceRepository.getProcessesWithState(ProcessInstanceState.ACTIVE.name()));

    processes.stream()
        .map(process -> PatternsCS.ask(processSupervisorActor,
            new ProcessWakeUpMessage.Request(process.getPiId()), Global.TIMEOUT)
            .toCompletableFuture())
        .forEach(this::handleProcessResponse);
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

}
