package at.fhjoanneum.ippr.processengine.akka.tasks.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import akka.actor.ActorRef;
import akka.actor.UntypedActorContext;
import akka.pattern.PatternsCS;
import at.fhjoanneum.ippr.persistence.objects.engine.process.ProcessInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.subject.Subject;
import at.fhjoanneum.ippr.processengine.akka.AkkaSelector;
import at.fhjoanneum.ippr.processengine.akka.config.Global;
import at.fhjoanneum.ippr.processengine.akka.config.SpringExtension;
import at.fhjoanneum.ippr.processengine.akka.messages.EmptyMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.initialize.ActorInitializeMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.initialize.UserActorInitializeMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.initialize.UserActorInitializeMessage.Request;
import at.fhjoanneum.ippr.processengine.akka.tasks.AbstractTask;
import at.fhjoanneum.ippr.processengine.repositories.ProcessInstanceRepository;

@Component("UserSupervisor.ProcessInitializeTask")
@Scope("prototype")
public class ProcessInitializeTask extends AbstractTask<ActorInitializeMessage.Request> {

  private final static Logger LOG = LoggerFactory.getLogger(ProcessInitializeTask.class);

  @Autowired
  private SpringExtension springExtension;
  @Autowired
  private AkkaSelector akkaSelector;

  @Autowired
  private ProcessInstanceRepository processInstanceRepository;

  public ProcessInitializeTask(final UntypedActorContext parentContext) {
    super(parentContext);
  }

  @Override
  public boolean canHandle(final Object obj) {
    return obj instanceof ActorInitializeMessage.Request;
  }

  @Override
  public void execute(final ActorInitializeMessage.Request msg) throws Exception {
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
        final Optional<ActorRef> actorOpt = akkaSelector.findActor(getParentContext(), userId);

        if (!actorOpt.isPresent()) {
          actors
              .add(
                  Pair.of(
                      getParentContext()
                          .actorOf(springExtension.props("UserActor", subject.getUser()), userId),
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
            getParentContext().actorOf(springExtension.props("UserActor", Global.DESTROY_ID),
                String.valueOf(UUID.randomUUID())),
            new UserActorInitializeMessage.Request(piId, subject.getSId())));
      }
    });
    return actors;
  }

  private String getUserId(final Long piId) {
    return "ProcessUser-" + piId;
  }
}
