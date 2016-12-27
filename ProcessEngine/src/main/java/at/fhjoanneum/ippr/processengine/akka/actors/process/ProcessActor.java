package at.fhjoanneum.ippr.processengine.akka.actors.process;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import akka.actor.UntypedActor;
import at.fhjoanneum.ippr.commons.dto.processengine.ProcessInfoDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.ProcessStateDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.SubjectStateDTO;
import at.fhjoanneum.ippr.persistence.objects.engine.enums.ProcessInstanceState;
import at.fhjoanneum.ippr.persistence.objects.engine.process.ProcessInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.state.SubjectState;
import at.fhjoanneum.ippr.persistence.objects.engine.subject.Subject;
import at.fhjoanneum.ippr.persistence.objects.model.enums.StateEventType;
import at.fhjoanneum.ippr.processengine.akka.messages.process.info.ProcessStateMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.stop.ProcessStopMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.workflow.StateObjectChangeMessage;
import at.fhjoanneum.ippr.processengine.repositories.ProcessInstanceRepository;

@Transactional
@Component("ProcessActor")
@Scope("prototype")
public class ProcessActor extends UntypedActor {

  private final static Logger LOG = LoggerFactory.getLogger(ProcessActor.class);

  @Autowired
  private ProcessInstanceRepository processInstanceRepository;

  private final Long piId;

  public ProcessActor(final Long piId) {
    this.piId = piId;
  }

  @Override
  public void onReceive(final Object obj) throws Throwable {
    if (obj instanceof ProcessStateMessage.Request) {
      handleProcessStateMessage(obj);
    } else if (obj instanceof ProcessStopMessage.Request) {
      handleProcessStopMessage(obj);
    } else if (obj instanceof StateObjectChangeMessage.Request) {
      handleStateObjectChangeMessage(obj);
    } else {
      unhandled(obj);
    }
  }

  private void handleProcessStateMessage(final Object obj) {
    final ProcessStateMessage.Request msg = (ProcessStateMessage.Request) obj;

    final Optional<ProcessInstance> processOpt =
        Optional.ofNullable(processInstanceRepository.findOne(msg.getPiId()));
    if (!processOpt.isPresent()) {
      getSender().tell(new akka.actor.Status.Failure(new IllegalArgumentException(
          "Could not find process instance for PI_ID: " + msg.getPiId())), getSelf());
    }

    final ProcessInstance process = processOpt.get();
    final List<SubjectStateDTO> subjectStates = process.getSubjects().stream()
        .map(this::convertToSubjectStateDTO).collect(Collectors.toList());

    getSender().tell(new ProcessStateMessage.Response(
        new ProcessStateDTO(process.getPiId(), process.getState().name(), process.getStartTime(),
            process.getEndTime(), subjectStates, process.getProcessModel().getName())),
        getSelf());;
  }

  private SubjectStateDTO convertToSubjectStateDTO(final Subject subject) {
    final String subjectName = subject.getSubjectModel().getName();
    final Long userId = subject.getUser();

    final SubjectState subjectState = subject.getSubjectState();
    final Long ssId = subjectState.getSsId();
    final String stateName = subjectState.getCurrentState().getName();
    final String functionType = subjectState.getCurrentState().getFunctionType().name();
    final String receiveSubjectState = subjectState.getReceiveSubjectState() != null
        ? subjectState.getReceiveSubjectState().name() : null;
    final LocalDateTime lastChanged = subjectState.getLastChanged();

    return new SubjectStateDTO(ssId, userId, subjectName, stateName, functionType,
        receiveSubjectState, lastChanged);
  }

  private void handleProcessStopMessage(final Object obj) {
    final ProcessStopMessage.Request msg = (ProcessStopMessage.Request) obj;

    final Optional<ProcessInstance> processOpt =
        Optional.ofNullable(processInstanceRepository.findOne(msg.getPiId()));
    if (!processOpt.isPresent()) {
      throw new IllegalArgumentException();
    }

    final ProcessInstance process = processOpt.get();
    if (!process.getState().equals(ProcessInstanceState.ACTIVE)) {
      throw new IllegalStateException();
    }

    process.setState(ProcessInstanceState.CANCELLED_BY_USER);

    TransactionSynchronizationManager
        .registerSynchronization(new TransactionSynchronizationAdapter() {
          @Override
          public void afterCommit() {
            LOG.info("Process was stopped: {}", process);
            final ProcessInfoDTO dto = new ProcessInfoDTO(process.getPiId(), process.getStartTime(),
                process.getEndTime(), process.getProcessModel().getName(), process.getStartUserId(),
                process.getState().name());
            getSender().tell(new ProcessStopMessage.Response(dto), getSelf());
            getContext().stop(getSelf());
          }
        });
  }

  private void handleStateObjectChangeMessage(final Object obj) {
    final StateObjectChangeMessage.Request request = (StateObjectChangeMessage.Request) obj;
    final Optional<ProcessInstance> processOpt =
        Optional.ofNullable(processInstanceRepository.findOne(request.getPiId()));

    if (processOpt.isPresent()) {
      final ProcessInstance process = processOpt.get();
      for (final Subject subject : process.getSubjects()) {
        if (!subject.getSubjectState().getCurrentState().getEventType()
            .equals(StateEventType.END)) {
          LOG.debug("State [{}] is not in 'END' state, cannot finish process instance",
              subject.getSubjectState().getCurrentState());
          getSender().tell(new StateObjectChangeMessage.Response(piId, Boolean.FALSE), getSelf());
          return;
        }
      }

      process.setState(ProcessInstanceState.FINISHED);
      LOG.info("All subject states are in 'END' state, set process instance to 'FINISHED'");
      getSender().tell(new StateObjectChangeMessage.Response(piId, Boolean.TRUE), getSelf());
      getContext().stop(getSelf());
    }
  }
}
