package at.fhjoanneum.ippr.persistence.entities.model.transition;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.base.Objects;

import at.fhjoanneum.ippr.persistence.entities.model.state.StateImpl;
import at.fhjoanneum.ippr.persistence.objects.model.enums.TransitionType;
import at.fhjoanneum.ippr.persistence.objects.model.state.State;
import at.fhjoanneum.ippr.persistence.objects.model.transition.Transition;

@Entity(name = "TRANSITION")
public class TransitionImpl implements Transition, Serializable {

  private static final long serialVersionUID = 5632015045248388049L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long tId;

  @ManyToOne
  @JoinColumn(name = "from_state")
  private StateImpl fromState;

  @ManyToOne
  @JoinColumn(name = "to_state")
  private StateImpl toState;

  @Column
  @NotNull
  @Enumerated(EnumType.STRING)
  private TransitionType transitionType;

  @Column
  private Long timeout;

  TransitionImpl() {}

  TransitionImpl(final StateImpl fromState, final StateImpl toState,
      final TransitionType transitionType) {
    this.fromState = fromState;
    this.toState = toState;
    if (transitionType == null) {
      this.transitionType = TransitionType.NORMAL;
    } else {
      this.transitionType = transitionType;
    }
  }

  TransitionImpl(final StateImpl fromState, final StateImpl toState,
      final TransitionType transitionType, final Long timeout) {
    this(fromState, toState, transitionType);
    this.timeout = timeout;
  }

  @Override
  public Long getTId() {
    return tId;
  }

  @Override
  public State getFromState() {
    return fromState;
  }

  @Override
  public State getToState() {
    return toState;
  }

  @Override
  public TransitionType getTransitionType() {
    return transitionType;
  }

  @Override
  public Long getTimeout() {
    return timeout;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null) {
      return false;
    }
    if (!Transition.class.isAssignableFrom(obj.getClass())) {
      return false;
    }
    final Transition other = (Transition) obj;
    if ((this.tId == null) ? (other.getTId() != null) : !this.tId.equals(other.getTId())) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(tId);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("sId", tId)
        .append("type", transitionType).append("fromState", fromState.getSId())
        .append("toState", toState.getSId()).toString();
  }
}
