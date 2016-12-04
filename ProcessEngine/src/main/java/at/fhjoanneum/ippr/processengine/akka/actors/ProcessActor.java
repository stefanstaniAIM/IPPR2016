package at.fhjoanneum.ippr.processengine.akka.actors;

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

import akka.actor.UntypedActor;
import at.fhjoanneum.ippr.commons.dto.processengine.ProcessStateDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.SubjectStateDTO;
import at.fhjoanneum.ippr.persistence.objects.engine.process.ProcessInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.state.SubjectState;
import at.fhjoanneum.ippr.persistence.objects.engine.subject.Subject;
import at.fhjoanneum.ippr.processengine.akka.messages.process.ProcessStateMessage;
import at.fhjoanneum.ippr.processengine.repositories.ProcessInstanceRepository;

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
    } else {
      unhandled(obj);
    }
  }

  @Transactional
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

    getSender().tell(
        new ProcessStateMessage.Response(
            new ProcessStateDTO(process.getPiId(), process.getState().name(), subjectStates)),
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
}
