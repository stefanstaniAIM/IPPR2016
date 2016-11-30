package at.fhjoanneum.ippr.persistence.objects.engine.state;

import java.time.LocalDateTime;

import at.fhjoanneum.ippr.persistence.entities.engine.enums.ReceiveSubjectState;
import at.fhjoanneum.ippr.persistence.objects.engine.process.ProcessInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.subject.Subject;
import at.fhjoanneum.ippr.persistence.objects.model.state.State;

public interface SubjectState {

  Long getSsId();

  State getCurrentState();

  void setCurrentState(final State currentState);

  ProcessInstance getProcessInstance();

  Subject getSubject();

  ReceiveSubjectState getReceiveSubjectState();

  void setReceiveSubjectState(ReceiveSubjectState receiveSubjectState);

  LocalDateTime getLastChanged();
}
