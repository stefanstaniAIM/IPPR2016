package at.fhjoanneum.ippr.processengine.akka.tasks.user;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;

import at.fhjoanneum.ippr.persistence.objects.engine.state.SubjectState;
import at.fhjoanneum.ippr.persistence.objects.model.enums.StateFunctionType;
import at.fhjoanneum.ippr.persistence.objects.model.messageflow.MessageFlow;
import at.fhjoanneum.ippr.persistence.objects.model.state.State;
import at.fhjoanneum.ippr.persistence.objects.model.subject.SubjectModel;
import at.fhjoanneum.ippr.persistence.objects.model.transition.Transition;
import at.fhjoanneum.ippr.processengine.akka.messages.process.timeout.TimeoutExecuteMessage;
import at.fhjoanneum.ippr.processengine.akka.tasks.AbstractTask;
import at.fhjoanneum.ippr.processengine.repositories.SubjectRepository;
import at.fhjoanneum.ippr.processengine.repositories.SubjectStateRepository;

@Component("User.ExecuteTimeoutTask")
@Scope("prototype")
public class ExecuteTimeoutTask extends AbstractTask<TimeoutExecuteMessage> {

  private final static Logger LOG = LoggerFactory.getLogger(ExecuteTimeoutTask.class);

  @Autowired
  private SubjectRepository subjectRepository;

  @Autowired
  private SubjectStateRepository subjectStateRepository;

  @Override
  public boolean canHandle(final Object obj) {
    return obj instanceof TimeoutExecuteMessage;
  }

  @Override
  public void execute(final TimeoutExecuteMessage message) throws Exception {
    final SubjectState subjectState = subjectStateRepository.findOne(message.getSsId());
    LOG.debug("Execute timeout for [{}]", subjectState);

    final Optional<Transition> timeoutTransitionOpt =
        subjectState.getCurrentState().getTimeoutTransition();
    if (!timeoutTransitionOpt.isPresent()) {
      throw new IllegalStateException(
          "Could not find timeout transition for [" + subjectState + "]");
    }

    final Transition timeoutTransition = timeoutTransitionOpt.get();
    subjectState.setCurrentState(timeoutTransition.getToState());
    LOG.info("Due to timeout new state for [{}]", subjectState);

    handleSendState(subjectState);

    subjectState.setTimeoutActor(null);
  }

  private void handleSendState(final SubjectState subjectState) {
    final State currentState = subjectState.getCurrentState();
    if (StateFunctionType.SEND.equals(currentState.getFunctionType())) {
      LOG.debug("Since new state is SEND, additional check for receiver has to be done [{}]",
          subjectState);

      currentState.getMessageFlow().stream().map(MessageFlow::getReceiver)
          .map(subjectModel -> subjectRepository.getSubjectForSubjectModelInProcess(
              subjectState.getProcessInstance().getPiId(), subjectModel.getSmId()))
          .filter(subject -> subject.getUser() != null).map(subject -> subject.getSubjectState())
          .forEach(receiveSubjectState -> {
            final State newState =
                getReceiveState(receiveSubjectState, currentState.getSubjectModel());
            receiveSubjectState.setCurrentState(newState);
            LOG.info("Due to timeout new state for [{}]", receiveSubjectState);
          });
    }
  }

  private State getReceiveState(final SubjectState receiveSubjectState,
      final SubjectModel sendSubjectModel) {
    Set<State> states = Sets.newHashSet(receiveSubjectState.getCurrentState());

    Optional<State> stateOpt = Optional.empty();
    while (!(stateOpt = findState(states, sendSubjectModel)).isPresent()) {
      states = receiveSubjectState.getCurrentState().getFromStates().stream()
          .map(Transition::getFromState).collect(Collectors.toSet());
    }
    return stateOpt.get();
  }

  private Optional<State> findState(final Set<State> states, final SubjectModel sendSubjectModel) {
    return states.stream().filter(state -> {
      if (StateFunctionType.RECEIVE.equals(state.getFunctionType())) {
        if (state.getMessageFlow().stream().map(MessageFlow::getSender)
            .filter(subjectModel -> subjectModel.equals(sendSubjectModel)).count() >= 1) {
          return true;
        }
      }
      return false;
    }).findFirst();
  }
}
