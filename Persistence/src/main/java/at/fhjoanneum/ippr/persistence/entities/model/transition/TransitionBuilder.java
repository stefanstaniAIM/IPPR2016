package at.fhjoanneum.ippr.persistence.entities.model.transition;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import at.fhjoanneum.ippr.persistence.builder.Builder;
import at.fhjoanneum.ippr.persistence.entities.model.state.StateImpl;
import at.fhjoanneum.ippr.persistence.objects.model.enums.TransitionType;
import at.fhjoanneum.ippr.persistence.objects.model.state.State;
import at.fhjoanneum.ippr.persistence.objects.model.transition.Transition;

public class TransitionBuilder implements Builder<Transition> {

  private StateImpl fromState;
  private StateImpl toState;
  private TransitionType transitionType;
  private Long timeout;

  public TransitionBuilder fromState(final State fromState) {
    checkNotNull(fromState);
    checkArgument(fromState instanceof StateImpl);
    this.fromState = (StateImpl) fromState;
    return this;
  }

  public TransitionBuilder toState(final State toState) {
    checkNotNull(toState);
    checkArgument(toState instanceof StateImpl);
    this.toState = (StateImpl) toState;
    return this;
  }

  public TransitionBuilder timeout(final Long timeout) {
    checkNotNull(toState);
    checkArgument(timeout.longValue() > 0);
    this.timeout = timeout;
    return this;
  }

  public TransitionBuilder transitionType(final TransitionType transitionType) {
    checkNotNull(transitionType);
    this.transitionType = transitionType;
    return this;
  }

  @Override
  public Transition build() {
    checkNotNull(fromState);
    checkNotNull(toState);
    if (TransitionType.AUTO_TIMEOUT.equals(transitionType)) {
      checkNotNull(timeout);
    }

    if (timeout == null) {
      return new TransitionImpl(fromState, toState, transitionType);
    } else {
      return new TransitionImpl(fromState, toState, transitionType, timeout);
    }
  }

}
