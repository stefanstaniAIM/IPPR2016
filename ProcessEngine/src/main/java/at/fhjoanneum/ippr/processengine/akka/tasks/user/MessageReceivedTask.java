package at.fhjoanneum.ippr.processengine.akka.tasks.user;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import akka.actor.ActorRef;
import akka.actor.Status;
import at.fhjoanneum.ippr.persistence.entities.engine.enums.SubjectSubState;
import at.fhjoanneum.ippr.persistence.entities.engine.state.SubjectStateImpl;
import at.fhjoanneum.ippr.persistence.objects.engine.state.SubjectState;
import at.fhjoanneum.ippr.processengine.akka.messages.process.workflow.MessageReceiveMessage;
import at.fhjoanneum.ippr.processengine.akka.tasks.AbstractTask;
import at.fhjoanneum.ippr.processengine.repositories.MessageFlowRepository;
import at.fhjoanneum.ippr.processengine.repositories.SubjectStateRepository;

@Component("User.MessageReceivedTask")
@Scope("prototype")
public class MessageReceivedTask extends AbstractTask<MessageReceiveMessage.Request> {

  private final static Logger LOG = LoggerFactory.getLogger(MessageReceivedTask.class);

  @Autowired
  private SubjectStateRepository subjectStateRepository;
  @Autowired
  private MessageFlowRepository messageFlowRepository;

  @Override
  public boolean canHandle(final Object obj) {
    return obj instanceof MessageReceiveMessage.Request;
  }

  @Override
  public void execute(final MessageReceiveMessage.Request request) throws Exception {
    LOG.debug("New received message for user [{}] in PI_ID [{}]", request.getUserId(),
        request.getPiId());
    final ActorRef sender = getSender();

    final Optional<SubjectState> subjectStateOpt = Optional.ofNullable(subjectStateRepository
        .getToReceiveSubjectStateOfUser(request.getPiId(), request.getUserId()));

    if (!subjectStateOpt.isPresent()) {
      sender.tell(
          new Status.Failure(new IllegalStateException("Could not find subject state for user ["
              + request.getUserId() + "] in PI_ID [" + request.getPiId() + "]")),
          getSelf());
      return;
    }

    final SubjectState subjectState = subjectStateOpt.get();
    if (SubjectSubState.RECEIVED.equals(subjectState.getSubState())) {
      LOG.debug("Message already received: {}", subjectState);
    } else {
      subjectState.setToReceived(messageFlowRepository.findOne(request.getMfId()));
      subjectStateRepository.save((SubjectStateImpl) subjectState);
      LOG.info("New received sub state: {}", subjectState);
    }

    TransactionSynchronizationManager
        .registerSynchronization(new TransactionSynchronizationAdapter() {
          @Override
          public void afterCommit() {
            sender.tell(new MessageReceiveMessage.Response(), getSelf());
          }
        });
  }
}
