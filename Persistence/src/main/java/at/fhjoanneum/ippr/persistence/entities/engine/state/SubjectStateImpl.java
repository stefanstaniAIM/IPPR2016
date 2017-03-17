package at.fhjoanneum.ippr.persistence.entities.engine.state;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import at.fhjoanneum.ippr.persistence.entities.engine.enums.SubjectSubState;
import at.fhjoanneum.ippr.persistence.entities.engine.process.ProcessInstanceImpl;
import at.fhjoanneum.ippr.persistence.entities.engine.subject.SubjectImpl;
import at.fhjoanneum.ippr.persistence.entities.model.messageflow.MessageFlowImpl;
import at.fhjoanneum.ippr.persistence.entities.model.state.StateImpl;
import at.fhjoanneum.ippr.persistence.objects.engine.state.SubjectState;
import at.fhjoanneum.ippr.persistence.objects.model.enums.StateFunctionType;
import at.fhjoanneum.ippr.persistence.objects.model.messageflow.MessageFlow;
import at.fhjoanneum.ippr.persistence.objects.model.state.State;

@Entity(name = "SUBJECT_STATE")
public class SubjectStateImpl implements SubjectState, Serializable {

  private static final long serialVersionUID = -8145174613972009727L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long ssId;

  @ManyToOne
  @JoinColumn(name = "current_state")
  private StateImpl currentState;

  @ManyToOne
  @JoinColumn(name = "pi_id")
  private ProcessInstanceImpl processInstance;

  @OneToOne // (mappedBy = "subjectState")
  @JoinColumn(name = "s_id")
  private SubjectImpl subject;

  @Column
  @Enumerated(EnumType.STRING)
  private SubjectSubState subState;

  @ManyToOne
  @JoinColumn(name = "mf_id")
  private MessageFlowImpl messageFlow;

  @Column
  @NotNull
  private LocalDateTime lastChanged;

  @Column
  private String timeoutActor;

  SubjectStateImpl() {}

  SubjectStateImpl(final StateImpl currentState, final ProcessInstanceImpl processInstance,
      final SubjectImpl subject) {
    this.processInstance = processInstance;
    this.subject = subject;
    this.lastChanged = LocalDateTime.now();
    setCurrentState(currentState, false);
  }

  SubjectStateImpl(final StateImpl currentState, final ProcessInstanceImpl processInstance,
      final SubjectImpl subject, final SubjectSubState subState) {
    this(currentState, processInstance, subject);
    this.subState = subState;
  }

  @Override
  public State getCurrentState() {
    return currentState;
  }

  @Override
  public void setCurrentState(final State nextState) {
    setCurrentState(nextState, false);
  }

  private void setCurrentState(final State nextState, final boolean isChecked) {
    checkNotNull(nextState);
    checkArgument(nextState instanceof StateImpl);
    if (isChecked) {
      checkArgument(isNextState(nextState));
    }
    currentState = (StateImpl) nextState;
    messageFlow = null;

    switch (currentState.getFunctionType()) {
      case RECEIVE:
        subState = SubjectSubState.TO_RECEIVE;
        break;
      case SEND:
        subState = SubjectSubState.TO_SEND;
        break;
      default:
        subState = null;
        break;
    }
    lastChanged = LocalDateTime.now();
  }

  @Override
  public void setToSent() {
    if (!StateFunctionType.SEND.equals(currentState.getFunctionType())) {
      throw new IllegalStateException();
    }

    if (SubjectSubState.TO_SEND.equals(subState) || SubjectSubState.NOTIFIED_EC.equals(subState)) {
      subState = SubjectSubState.SENT;
      lastChanged = LocalDateTime.now();
    }
  }

  @Override
  public void setToNotifiedEC() {
    if (!StateFunctionType.SEND.equals(currentState.getFunctionType())) {
      throw new IllegalStateException();
    }

    if (SubjectSubState.TO_SEND.equals(subState)) {
      subState = SubjectSubState.NOTIFIED_EC;
      lastChanged = LocalDateTime.now();
    }
  }

  @Override
  public void setToReceived(final MessageFlow messageFlow) {
    if (!StateFunctionType.RECEIVE.equals(currentState.getFunctionType())) {
      throw new IllegalStateException();
    }
    checkNotNull(messageFlow);

    if (SubjectSubState.TO_RECEIVE.equals(subState)) {
      subState = SubjectSubState.RECEIVED;
      this.messageFlow = (MessageFlowImpl) messageFlow;
      lastChanged = LocalDateTime.now();
    }
  }

  @Override
  public boolean isNextState(final State nextState) {
    return currentState.getToStates().stream()
        .filter(transition -> transition.getToState().equals(nextState)).count() >= 1;
  }

  @Override
  public void setTimeoutActor(final String timeoutActor) {
    this.timeoutActor = timeoutActor;
  }

  @Override
  public String getTimeoutActor() {
    return timeoutActor;
  }

  @Override
  public MessageFlow getCurrentMessageFlow() {
    return messageFlow;
  }

  @Override
  public Long getSsId() {
    return ssId;
  }

  @Override
  public ProcessInstanceImpl getProcessInstance() {
    return processInstance;
  }

  @Override
  public SubjectImpl getSubject() {
    return subject;
  }

  @Override
  public SubjectSubState getSubState() {
    return subState;
  }

  @Override
  public LocalDateTime getLastChanged() {
    return lastChanged;
  }

  @Override
  public int hashCode() {
    return Objects.hash(ssId);
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final SubjectStateImpl other = (SubjectStateImpl) obj;
    if (ssId == null) {
      if (other.ssId != null) {
        return false;
      }
    } else if (!ssId.equals(other.ssId)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("ssId", ssId)
        .append("subject", subject).append("currentState", currentState.getName())
        .append("subState", subState != null ? subState.name() : StringUtils.EMPTY).toString();
  }
}
