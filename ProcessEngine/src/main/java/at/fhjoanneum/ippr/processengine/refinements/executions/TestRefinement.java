package at.fhjoanneum.ippr.processengine.refinements.executions;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.fhjoanneum.ippr.processengine.refinements.core.AbstractRefinement;
import at.fhjoanneum.ippr.processengine.refinements.core.BusinessObject;
import at.fhjoanneum.ippr.processengine.refinements.core.BusinessObjectField;

public class TestRefinement extends AbstractRefinement {

  private final static Logger LOG = LoggerFactory.getLogger(TestRefinement.class);

  @Override
  public Map<String, BusinessObject> execute(final Map<String, BusinessObject> businessObjects) {
    final BusinessObject businessObject = businessObjects.get("refinement bo");
    final BusinessObjectField field = businessObject.getFields().get("write field");
    field.setValue("refinement task");
    return businessObjects;
  }

  @Override
  public Long getNextStateId(final Map<String, Long> nextStates) {
    return nextStates.get("END");
  }

}
