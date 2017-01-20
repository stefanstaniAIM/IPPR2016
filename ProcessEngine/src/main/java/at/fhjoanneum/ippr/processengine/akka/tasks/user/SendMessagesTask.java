package at.fhjoanneum.ippr.processengine.akka.tasks.user;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.google.common.collect.Iterables;

import akka.actor.ActorRef;
import akka.actor.Status;
import akka.actor.UntypedActorContext;
import akka.pattern.PatternsCS;
import at.fhjoanneum.ippr.persistence.entities.engine.state.SubjectStateImpl;
import at.fhjoanneum.ippr.persistence.objects.engine.state.SubjectState;
import at.fhjoanneum.ippr.processengine.akka.AkkaSelector;
import at.fhjoanneum.ippr.processengine.akka.config.Global;
import at.fhjoanneum.ippr.processengine.akka.config.SpringExtension;
import at.fhjoanneum.ippr.processengine.akka.messages.process.workflow.MessageReceiveMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.workflow.MessagesSendMessage;
import at.fhjoanneum.ippr.processengine.akka.tasks.AbstractTask;
import at.fhjoanneum.ippr.processengine.repositories.SubjectStateRepository;

@Component("UserSupervisor.SendMessagesTask")
@Scope("prototype")
public class SendMessagesTask extends AbstractTask<MessagesSendMessage.Request> {

  private final static Logger LOG = LoggerFactory.getLogger(SendMessagesTask.class);

  @Autowired
  private AkkaSelector akkaSelector;
  @Autowired
  private SpringExtension springExtension;

  @Autowired
  private SubjectStateRepository subjectStateRepository;

  @PersistenceContext
  private EntityManager entityManager;

  public SendMessagesTask(final UntypedActorContext parentContext) {
    super(parentContext);
  }

  @Override
  public boolean canHandle(final Object obj) {
    return obj instanceof MessagesSendMessage.Request;
  }

  @Override
  public void execute(final MessagesSendMessage.Request request) throws Exception {
    final List<CompletableFuture<Object>> futures =
        request.getUserMessageFlowIds()
            .stream().map(userMessageFlow -> convertToFuture(request.getPiId(),
                userMessageFlow.getLeft(), userMessageFlow.getRight()))
            .collect(Collectors.toList());

    final ActorRef sender = getSender();

    try {
      CompletableFuture.allOf(Iterables.toArray(futures, CompletableFuture.class)).get();
      LOG.info("All users received the message in PI_ID [{}]", request.getPiId());
      final SubjectState sendState = subjectStateRepository.findOne(request.getSendSubjectState());
      sendState.setToSent();
      subjectStateRepository.save((SubjectStateImpl) sendState);
      LOG.debug("{} is set to 'SENT'", sendState);
    } catch (final Exception e) {
      LOG.error("At least one user did not receive the message in PI_ID [{}]", request.getPiId());
      sender.tell(
          new Status.Failure(new IllegalStateException(
              "Could not send message to all users in PI_ID [" + request.getPiId() + "]")),
          getSelf());
    }

    TransactionSynchronizationManager
        .registerSynchronization(new TransactionSynchronizationAdapter() {
          @Override
          public void afterCommit() {
            sender.tell(new MessagesSendMessage.Response(), getSelf());
          }
        });
  }

  private CompletableFuture<Object> convertToFuture(final Long piId, final Long userId,
      final Long mfId) {
    final Optional<ActorRef> userActorOpt =
        akkaSelector.findActor(getParentContext(), getUserId(userId));
    ActorRef userActor = null;
    if (userActorOpt.isPresent()) {
      userActor = userActorOpt.get();
    } else {
      userActor =
          getParentContext().actorOf(springExtension.props("UserActor", userId), getUserId(userId));
    }
    return PatternsCS
        .ask(userActor, new MessageReceiveMessage.Request(piId, userId, mfId), Global.TIMEOUT)
        .toCompletableFuture();
  }

  private String getUserId(final Long userId) {
    return "ProcessUser-" + userId;
  }
}
