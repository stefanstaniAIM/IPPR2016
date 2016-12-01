package at.fhjoanneum.ippr.processengine.akka.actors;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import akka.actor.UntypedActor;
import at.fhjoanneum.ippr.persistence.entities.engine.enums.ReceiveSubjectState;
import at.fhjoanneum.ippr.persistence.entities.engine.state.SubjectStateBuilder;
import at.fhjoanneum.ippr.persistence.entities.engine.state.SubjectStateImpl;
import at.fhjoanneum.ippr.persistence.objects.engine.process.ProcessInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.state.SubjectState;
import at.fhjoanneum.ippr.persistence.objects.engine.subject.Subject;
import at.fhjoanneum.ippr.persistence.objects.model.enums.StateFunctionType;
import at.fhjoanneum.ippr.persistence.objects.model.state.State;
import at.fhjoanneum.ippr.processengine.akka.messages.EmptyMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.ActorInitializeMessage;
import at.fhjoanneum.ippr.processengine.repositories.ProcessInstanceRepository;
import at.fhjoanneum.ippr.processengine.repositories.StateRepository;
import at.fhjoanneum.ippr.processengine.repositories.SubjectRepository;
import at.fhjoanneum.ippr.processengine.repositories.SubjectStateRepository;

@Component("UserActor")
@Scope("prototype")
public class UserActor extends UntypedActor {

  private final static Logger LOG = LoggerFactory.getLogger(UserActor.class);

  @Autowired
  private ProcessInstanceRepository processInstanceRepository;
  @Autowired
  private SubjectRepository subjectRepository;
  @Autowired
  private StateRepository stateRepository;
  @Autowired
  private SubjectStateRepository subjectStateRepository;

  private final Long userId;

  public UserActor(final Long userId) {
    this.userId = userId;
  }

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

    final ProcessInstance processInstance =
        Optional.ofNullable(processInstanceRepository.findOne(msg.getProcessId())).get();

    final Subject subject = Optional
        .ofNullable(subjectRepository.getSubjectForUserInProcess(msg.getProcessId(), userId)).get();

    final State state = Optional
        .ofNullable(stateRepository.getStartStateOfSubject(subject.getSubjectModel().getSmId()))
        .get();

    ReceiveSubjectState receiveSubjectState = null;
    if (state.getFunctionType().equals(StateFunctionType.RECEIVE)) {
      receiveSubjectState = ReceiveSubjectState.TO_RECEIVE;
    }

    final SubjectState subjectState = new SubjectStateBuilder().processInstance(processInstance)
        .subject(subject).state(state).receiveSubjectState(receiveSubjectState).build();

    subjectStateRepository.save((SubjectStateImpl) subjectState);
    LOG.info("Subject is now in initial state: {}", subjectState);

    // notify user supervisor actor
    getSender().tell(new EmptyMessage(), getSelf());
  }

}
