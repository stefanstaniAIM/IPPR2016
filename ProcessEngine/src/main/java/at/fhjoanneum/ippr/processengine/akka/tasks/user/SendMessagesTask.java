package at.fhjoanneum.ippr.processengine.akka.tasks.user;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.Iterables;

import akka.actor.ActorRef;
import akka.actor.UntypedActorContext;
import akka.pattern.PatternsCS;
import at.fhjoanneum.ippr.processengine.akka.AkkaSelector;
import at.fhjoanneum.ippr.processengine.akka.config.Global;
import at.fhjoanneum.ippr.processengine.akka.config.SpringExtension;
import at.fhjoanneum.ippr.processengine.akka.messages.process.wakeup.UserActorWakeUpMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.workflow.SendMessages;
import at.fhjoanneum.ippr.processengine.akka.tasks.AbstractTask;

@Component("UserSupervisor.SendMessagesTask")
@Scope("prototype")
public class SendMessagesTask extends AbstractTask {

  private final static Logger LOG = LoggerFactory.getLogger(SendMessagesTask.class);

  @Autowired
  private AkkaSelector akkaSelector;
  @Autowired
  private SpringExtension springExtension;

  public SendMessagesTask(final UntypedActorContext parentContext) {
    super(parentContext);
  }

  @Override
  public boolean canHandle(final Object obj) {
    return obj instanceof SendMessages.Request;
  }

  @Override
  public void execute(final Object obj) throws Exception {
    final SendMessages.Request request = (SendMessages.Request) obj;

    final List<CompletableFuture<Object>> futures = request.getUserIds().stream()
        .map(userId -> convertToFuture(request, userId)).collect(Collectors.toList());

    CompletableFuture.allOf(Iterables.toArray(futures, CompletableFuture.class))
        .whenComplete((msg, exc) -> {
          if (exc == null) {
            LOG.info("All users received the message in PI_ID [{}]", request.getPiId());
            getSender().tell(new SendMessages.Response(), getSelf());
          } else {
            LOG.error("At least one user did not receive the message in PI_ID [{}]");
            getSender().tell(
                new akka.actor.Status.Failure(new IllegalStateException(
                    "Could not send message to all users in PI_ID [" + request.getPiId() + "]")),
                getSelf());
          }
        });
  }

  private CompletableFuture<Object> convertToFuture(final SendMessages.Request request,
      final Long userId) {
    final Optional<ActorRef> userActorOpt =
        akkaSelector.findActor(getParentContext(), getUserId(userId));
    ActorRef userActor = null;
    if (userActorOpt.isPresent()) {
      userActor = userActorOpt.get();
    } else {
      userActor =
          getContext().actorOf(springExtension.props("UserActor", userId), getUserId(userId));
    }
    return PatternsCS.ask(userActor, new UserActorWakeUpMessage.Request(userId), Global.TIMEOUT)
        .toCompletableFuture();
  }

  private String getUserId(final Long userId) {
    return "ProcessUser-" + userId;
  }
}
