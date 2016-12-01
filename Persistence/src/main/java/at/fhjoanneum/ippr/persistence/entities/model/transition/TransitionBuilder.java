package at.fhjoanneum.ippr.persistence.entities.model.transition;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import at.fhjoanneum.ippr.persistence.builder.Builder;
import at.fhjoanneum.ippr.persistence.entities.model.state.StateImpl;
import at.fhjoanneum.ippr.persistence.objects.model.state.State;
import at.fhjoanneum.ippr.persistence.objects.model.transition.Transition;

public class TransitionBuilder implements Builder<Transition> {

  private StateImpl fromState;
  private StateImpl toState;
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
    this.timeout = timeout;
    return this;
  }

  @Override
  public Transition build() {
    checkNotNull(fromState);
    checkNotNull(toState);

    if (timeout != null) {
      return new TransitionImpl(fromState, toState, timeout);
    }
    return new TransitionImpl(fromState, toState);
  }

}
