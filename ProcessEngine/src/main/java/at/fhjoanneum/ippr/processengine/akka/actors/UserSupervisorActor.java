package at.fhjoanneum.ippr.processengine.akka.actors;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.Iterables;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.pattern.PatternsCS;
import at.fhjoanneum.ippr.persistence.objects.engine.process.ProcessInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.subject.Subject;
import at.fhjoanneum.ippr.processengine.akka.AkkaSelector;
import at.fhjoanneum.ippr.processengine.akka.config.Global;
import at.fhjoanneum.ippr.processengine.akka.config.SpringExtension;
import at.fhjoanneum.ippr.processengine.akka.messages.process.ActorInitializeMessage;
import at.fhjoanneum.ippr.processengine.repositories.ProcessInstanceRepository;

@Component("UserSupervisorActor")
@Scope("prototype")
public class UserSupervisorActor extends UntypedActor {

  private final static Logger LOG = LoggerFactory.getLogger(UserSupervisorActor.class);

  @Autowired
  private AkkaSelector akkaSelector;

  @Autowired
  private ProcessInstanceRepository processInstanceRepository;

  @Autowired
  private SpringExtension springExtension;

  @Override
  public void onReceive(final Object obj) throws Throwable {
    if (obj instanceof ActorInitializeMessage.Request) {
      handleActorInitializeMessage(obj);
    } else {
      unhandled(obj);
    }
  }

  private void handleActorInitializeMessage(final Object obj) {
    final ActorInitializeMessage.Request msg = (ActorInitializeMessage.Request) obj;

    final Optional<ProcessInstance> processInstanceOpt =
        Optional.ofNullable(processInstanceRepository.findOne(msg.getProcessId()));

    if (!processInstanceOpt.isPresent()) {
      throw new IllegalStateException(
          "Could not find process instance for P_ID [" + msg.getProcessId() + "]");
    }

    final ProcessInstance processInstance = processInstanceOpt.get();
    final List<ActorRef> actors = getActors(processInstance.getSubjects());
    LOG.debug("Send ActorInitializeMessage.Request to actors: {}", actors);

    final List<CompletableFuture<Object>> futures = actors.stream()
        .map(actor -> PatternsCS.ask(actor, msg, Global.TIMEOUT).toCompletableFuture())
        .collect(Collectors.toList());


    final ActorRef service = getSender();

    CompletableFuture.allOf(Iterables.toArray(futures, CompletableFuture.class))
        .whenComplete((res, exc) -> {
          if (exc != null) {
            LOG.error("Could not initialize the subjects to start state");
          } else {
            LOG.info("All subjects are in start state, notify service");
            service.tell(new Object(), getSelf());
          }
        });
  }

  private List<ActorRef> getActors(final List<Subject> subjects) {
    final List<ActorRef> processUsers = Lists.newArrayList();

    subjects.forEach(subject -> {
      if (subject.getUser() != null) {
        final String userId = "ProcessUser-" + subject.getUser();
        LOG.debug("Try to find or create new actor for: {}", userId);
        final Optional<ActorRef> actorOpt = akkaSelector.findActor(getContext(), userId);

        if (!actorOpt.isPresent()) {
          processUsers.add(getContext()
              .actorOf(springExtension.props("ProcessUserActor", subject.getUser()), userId));
        } else {
          processUsers.add(actorOpt.get());
        }
      } else {
        // TODO add group support
      }
    });
    return processUsers;
  }

}
