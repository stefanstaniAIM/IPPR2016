package at.fhjoanneum.ippr.processengine.refinements.core;

import java.util.Map;

public interface Refinement {
  Map<String, BusinessObject> execute(Map<String, BusinessObject> businessObjects);

  Long getNextStateId(Map<String, Long> nextStates);
}
