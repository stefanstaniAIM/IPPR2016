package at.fhjoanneum.ippr.persistence.objects.engine.state;

import java.time.LocalDateTime;

import at.fhjoanneum.ippr.persistence.entities.engine.enums.SubjectSubState;
import at.fhjoanneum.ippr.persistence.objects.engine.process.ProcessInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.subject.Subject;
import at.fhjoanneum.ippr.persistence.objects.model.messageflow.MessageFlow;
import at.fhjoanneum.ippr.persistence.objects.model.state.State;

public interface SubjectState {

  Long getSsId();

  State getCurrentState();

  void setCurrentState(final State currentState);

  ProcessInstance getProcessInstance();

  Subject getSubject();

  SubjectSubState getSubState();

  void setToSent();

  void setToNotifiedEC();

  void setToReceived(MessageFlow messageFlow);

  MessageFlow getCurrentMessageFlow();

  LocalDateTime getLastChanged();

  boolean isNextState(final State nextState);

  void setTimeoutActor(final String timeoutActor);

  String getTimeoutActor();
}
