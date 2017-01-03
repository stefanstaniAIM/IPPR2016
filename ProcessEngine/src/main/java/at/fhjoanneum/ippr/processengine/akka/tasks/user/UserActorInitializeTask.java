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
import at.fhjoanneum.ippr.persistence.entities.engine.enums.SubjectSubState;
import at.fhjoanneum.ippr.persistence.entities.engine.state.SubjectStateBuilder;
import at.fhjoanneum.ippr.persistence.entities.engine.state.SubjectStateImpl;
import at.fhjoanneum.ippr.persistence.objects.engine.process.ProcessInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.state.SubjectState;
import at.fhjoanneum.ippr.persistence.objects.engine.subject.Subject;
import at.fhjoanneum.ippr.persistence.objects.model.enums.StateFunctionType;
import at.fhjoanneum.ippr.persistence.objects.model.state.State;
import at.fhjoanneum.ippr.processengine.akka.messages.EmptyMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.initialize.UserActorInitializeMessage;
import at.fhjoanneum.ippr.processengine.akka.tasks.AbstractTask;
import at.fhjoanneum.ippr.processengine.akka.tasks.TaskCallback;
import at.fhjoanneum.ippr.processengine.repositories.ProcessInstanceRepository;
import at.fhjoanneum.ippr.processengine.repositories.StateRepository;
import at.fhjoanneum.ippr.processengine.repositories.SubjectRepository;
import at.fhjoanneum.ippr.processengine.repositories.SubjectStateRepository;

@Component("User.UserActorInitializeTask")
@Scope("prototype")
public class UserActorInitializeTask extends AbstractTask {

  private final static Logger LOG = LoggerFactory.getLogger(StateObjectRetrieveTask.class);

  @Autowired
  private ProcessInstanceRepository processInstanceRepository;
  @Autowired
  private SubjectRepository subjectRepository;
  @Autowired
  private StateRepository stateRepository;
  @Autowired
  private SubjectStateRepository subjectStateRepository;

  public <T> UserActorInitializeTask(final TaskCallback<T> callback) {
    super(callback);
  }

  @Override
  public boolean canHandle(final Object obj) {
    return obj instanceof UserActorInitializeMessage.Request;
  }

  @Override
  public void execute(final Object obj) throws Exception {
    final UserActorInitializeMessage.Request msg = (UserActorInitializeMessage.Request) obj;

    final ProcessInstance processInstance =
        Optional.ofNullable(processInstanceRepository.findOne(msg.getPiId())).get();

    final Subject subject = Optional.ofNullable(subjectRepository.findOne(msg.getSId())).get();

    final State state = Optional
        .ofNullable(stateRepository.getStartStateOfSubject(subject.getSubjectModel().getSmId()))
        .get();

    SubjectSubState subState = null;
    if (state.getFunctionType().equals(StateFunctionType.RECEIVE)) {
      subState = SubjectSubState.TO_RECEIVE;
    } else if (state.getFunctionType().equals(StateFunctionType.SEND)) {
      subState = SubjectSubState.TO_SEND;
    }

    final SubjectState subjectState = new SubjectStateBuilder().processInstance(processInstance)
        .subject(subject).state(state).subState(subState).build();

    subjectStateRepository.save((SubjectStateImpl) subjectState);
    LOG.info("Subject is now in initial state: {}", subjectState);

    final ActorRef sender = getSender();
    TransactionSynchronizationManager
        .registerSynchronization(new TransactionSynchronizationAdapter() {
          @Override
          public void afterCommit() {
            // notify user supervisor actor
            sender.tell(new EmptyMessage(), getSelf());
            callback();
          }
        });
  }

}
