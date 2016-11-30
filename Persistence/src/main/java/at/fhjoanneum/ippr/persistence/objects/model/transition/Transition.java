package at.fhjoanneum.ippr.persistence.objects.model.transition;

import at.fhjoanneum.ippr.persistence.objects.model.state.State;

public interface Transition {
  Long getTId();

  State getFromState();

  State getToState();

  Long getTimeout();
}
