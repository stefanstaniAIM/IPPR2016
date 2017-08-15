package at.fhjoanneum.ippr.processengine.akka.tasks.user;

import akka.actor.ActorRef;
import akka.actor.Status;
import at.fhjoanneum.ippr.commons.dto.eventlogger.EventLoggerDTO;
import at.fhjoanneum.ippr.persistence.entities.engine.enums.SubjectSubState;
import at.fhjoanneum.ippr.persistence.entities.engine.state.SubjectStateImpl;
import at.fhjoanneum.ippr.persistence.objects.engine.state.SubjectState;
import at.fhjoanneum.ippr.persistence.objects.model.enums.StateFunctionType;
import at.fhjoanneum.ippr.persistence.objects.model.messageflow.MessageFlow;
import at.fhjoanneum.ippr.processengine.akka.messages.process.timeout.TimeoutScheduleCancelMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.workflow.MessageReceiveMessage;
import at.fhjoanneum.ippr.processengine.akka.tasks.AbstractTask;
import at.fhjoanneum.ippr.processengine.repositories.MessageFlowRepository;
import at.fhjoanneum.ippr.processengine.repositories.SubjectStateRepository;
import at.fhjoanneum.ippr.processengine.services.EventLoggerSender;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Optional;

@Component("User.MessageReceivedTask")
@Scope("prototype")
public class MessageReceivedTask extends AbstractTask<MessageReceiveMessage.Request> {

  private final static Logger LOG = LoggerFactory.getLogger(MessageReceivedTask.class);

  @Autowired
  private SubjectStateRepository subjectStateRepository;
  @Autowired
  private MessageFlowRepository messageFlowRepository;
  @Autowired
  private EventLoggerSender eventLoggerSender;

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
      if (subjectState.getTimeoutActor() != null) {
        getContext().parent().tell(new TimeoutScheduleCancelMessage(subjectState.getSsId()),
            getSelf());
      }

      // Log Receive Event
      final long caseId = subjectState.getProcessInstance().getPiId();
      final long processModelId = subjectState.getProcessInstance().getProcessModel().getPmId();
      final String activity = subjectState.getCurrentState().getName();
      final String state = StateFunctionType.RECEIVE.name();
      final String resource = subjectState.getSubject().getSubjectModel().getName();
      final String timestamp = DateTime.now().toString("dd.MM.yyyy HH:mm");

      final MessageFlow messageFlow = messageFlowRepository.findOne(request.getMfId());
      final String messageType = messageFlow
          .getBusinessObjectModels().get(0).getName();
      final String recipient = resource;
      final String msgSender = messageFlow.getSender().getName();

      final EventLoggerDTO event = new EventLoggerDTO(caseId, processModelId, timestamp, activity,
          resource, state, messageType, recipient, msgSender);
      eventLoggerSender.send(event);

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
