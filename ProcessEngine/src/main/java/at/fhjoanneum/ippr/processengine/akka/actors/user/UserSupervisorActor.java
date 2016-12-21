package at.fhjoanneum.ippr.processengine.akka.actors.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.pattern.PatternsCS;
import at.fhjoanneum.ippr.persistence.objects.engine.process.ProcessInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.subject.Subject;
import at.fhjoanneum.ippr.processengine.akka.AkkaSelector;
import at.fhjoanneum.ippr.processengine.akka.config.Global;
import at.fhjoanneum.ippr.processengine.akka.config.SpringExtension;
import at.fhjoanneum.ippr.processengine.akka.messages.EmptyMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.info.TasksOfUserMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.initialize.ActorInitializeMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.initialize.UserActorInitializeMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.initialize.UserActorInitializeMessage.Request;
import at.fhjoanneum.ippr.processengine.akka.messages.process.stop.ProcessStopMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.wakeup.UserActorWakeUpMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.workflow.StateObjectMessage;
import at.fhjoanneum.ippr.processengine.repositories.ProcessInstanceRepository;

@Transactional
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
    } else if (obj instanceof UserActorWakeUpMessage.Request) {
      handleUserWakeUpMessage(obj);
    } else if (obj instanceof ProcessStopMessage.Request) {
      handleProcessStopMessage(obj);
    } else if (obj instanceof TasksOfUserMessage.Request) {
      handleTasksOfUserMessage(obj);
    } else if (obj instanceof StateObjectMessage.Request) {
      handleStateObjectMessage(obj);
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
    final List<Pair<ActorRef, Request>> actorsWithMessage =
        getActorsWithMessage(processInstance.getPiId(), processInstance.getSubjects());
    LOG.debug("Send ActorInitializeMessage.Request to actors: {}", actorsWithMessage);

    final List<CompletableFuture<Object>> futures = actorsWithMessage.stream()
        .map(pair -> PatternsCS.ask(pair.getLeft(), pair.getRight(), Global.TIMEOUT)
            .toCompletableFuture())
        .collect(Collectors.toList());

    final ActorRef service = getSender();

    CompletableFuture.allOf(Iterables.toArray(futures, CompletableFuture.class))
        .whenComplete((res, exc) -> {
          if (exc != null) {
            LOG.error("Could not initialize the subjects to start state");
          } else {
            LOG.info("All subjects are in start state, notify service");
            service.tell(new EmptyMessage(), getSelf());
          }
        });
  }

  private List<Pair<ActorRef, UserActorInitializeMessage.Request>> getActorsWithMessage(
      final Long piId, final List<Subject> subjects) {
    final List<Pair<ActorRef, UserActorInitializeMessage.Request>> actors = Lists.newArrayList();

    subjects.forEach(subject -> {
      if (subject.getUser() != null) {
        final String userId = getUserId(subject.getUser());
        LOG.debug("Try to find or create new actor for: {}", userId);
        final Optional<ActorRef> actorOpt = akkaSelector.findActor(getContext(), userId);

        if (!actorOpt.isPresent()) {
          actors.add(Pair.of(
              getContext().actorOf(springExtension.props("UserActor", subject.getUser()), userId),
              new UserActorInitializeMessage.Request(piId, subject.getSId())));
        } else {
          actors.add(Pair.of(actorOpt.get(),
              new UserActorInitializeMessage.Request(piId, subject.getSId())));
        }
      } else if (subject.getGroup() != null) {
        // TODO add group support
      } else {
        LOG.debug("Will create temporary actor since user or group is not set");
        actors.add(Pair.of(
            getContext().actorOf(springExtension.props("UserActor", Global.DESTROY_ID),
                String.valueOf(UUID.randomUUID())),
            new UserActorInitializeMessage.Request(piId, subject.getSId())));
      }
    });
    return actors;
  }

  private String getUserId(final Long piId) {
    return "ProcessUser-" + piId;
  }

  private void handleUserWakeUpMessage(final Object obj) {
    final UserActorWakeUpMessage.Request msg = (UserActorWakeUpMessage.Request) obj;
    final String userId = getUserId(msg.getUserId());
    final Optional<ActorRef> actorOpt = akkaSelector.findActor(getContext(), userId);

    ActorRef actor = null;
    if (!actorOpt.isPresent()) {
      actor = getContext().actorOf(springExtension.props("UserActor", msg.getUserId()), userId);
    } else {
      actor = actorOpt.get();
    }
    actor.forward(msg, getContext());
  }

  private void handleProcessStopMessage(final Object obj) {
    final ProcessStopMessage.Request msg = (ProcessStopMessage.Request) obj;
    final Optional<ProcessInstance> processOpt =
        Optional.ofNullable(processInstanceRepository.findOne(msg.getPiId()));

    if (processOpt.isPresent()) {
      final ProcessInstance process = processOpt.get();
      final List<String> userIds =
          process.getSubjects().stream().filter(subject -> subject.getUser() != null)
              .map(subject -> "ProcessUser-" + subject.getUser()).collect(Collectors.toList());

      userIds.forEach(userId -> {
        final Optional<ActorRef> actorOpt = akkaSelector.findActor(getContext(), userId);
        if (actorOpt.isPresent()) {
          actorOpt.get().forward(msg, getContext());
        }
      });
    }
  }

  private void handleTasksOfUserMessage(final Object obj) {
    final TasksOfUserMessage.Request msg = (TasksOfUserMessage.Request) obj;

    final String userId = getUserId(msg.getUserId());
    final Optional<ActorRef> actorOpt = akkaSelector.findActor(getContext(), userId);
    if (!actorOpt.isPresent()) {
      getSender().tell(new TasksOfUserMessage.Response(Lists.newArrayList()), getSelf());
      return;
    }

    actorOpt.get().forward(msg, getContext());
  }

  private void handleStateObjectMessage(final Object obj) {
    final StateObjectMessage.Request request = (StateObjectMessage.Request) obj;
    LOG.info("Handle state object message of USER_ID [{}] in PI_ID [{}]", request.getUserId(),
        request.getPiId());

    final String userId = getUserId(request.getUserId());
    final Optional<ActorRef> actorOpt = akkaSelector.findActor(getContext(), userId);

    if (actorOpt.isPresent()) {
      LOG.debug("Found user actor and will forward message");
      actorOpt.get().forward(request, getContext());
    } else {
      getSender().tell(new akka.actor.Status.Failure(
          new IllegalArgumentException("Could not find actor for user: " + userId)), getSelf());
      return;
    }
  }
}
