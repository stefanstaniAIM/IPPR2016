package at.fhjoanneum.ippr.processengine.akka.tasks.process;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import at.fhjoanneum.ippr.commons.dto.processengine.ProcessStateDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.SubjectStateDTO;
import at.fhjoanneum.ippr.persistence.objects.engine.process.ProcessInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.state.SubjectState;
import at.fhjoanneum.ippr.persistence.objects.engine.subject.Subject;
import at.fhjoanneum.ippr.persistence.objects.model.enums.SubjectModelType;
import at.fhjoanneum.ippr.processengine.akka.messages.process.info.ProcessStateMessage;
import at.fhjoanneum.ippr.processengine.akka.tasks.AbstractTask;
import at.fhjoanneum.ippr.processengine.repositories.ProcessInstanceRepository;

@Component("Process.ProcessStateTask")
@Scope("prototype")
public class ProcessStateTask extends AbstractTask<ProcessStateMessage.Request> {

  private final static Logger LOG = LoggerFactory.getLogger(ProcessStateTask.class);

  @Autowired
  private ProcessInstanceRepository processInstanceRepository;

  @Override
  public boolean canHandle(final Object obj) {
    return obj instanceof ProcessStateMessage.Request;
  }

  @Override
  public void execute(final ProcessStateMessage.Request msg) throws Exception {
    final Optional<ProcessInstance> processOpt =
        Optional.ofNullable(processInstanceRepository.findOne(msg.getPiId()));
    if (!processOpt.isPresent()) {
      getSender().tell(new akka.actor.Status.Failure(new IllegalArgumentException(
          "Could not find process instance for PI_ID: " + msg.getPiId())), getSelf());
    }

    final ProcessInstance process = processOpt.get();
    final List<SubjectStateDTO> subjectStates = process.getSubjects().stream()
        .filter(subject -> subject.getSubjectModel().getSubjectModelType()
            .equals(SubjectModelType.INTERNAL))
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
    final String receiveSubjectState =
        subjectState.getSubState() != null ? subjectState.getSubState().name() : null;
    final LocalDateTime lastChanged = subjectState.getLastChanged();

    return new SubjectStateDTO(ssId, userId, subjectName, stateName, functionType,
        receiveSubjectState, lastChanged);
  }
}
