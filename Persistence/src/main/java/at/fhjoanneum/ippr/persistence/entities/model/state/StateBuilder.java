package at.fhjoanneum.ippr.persistence.entities.model.state;

import static at.fhjoanneum.ippr.persistence.builder.BuilderUtils.isNotBlank;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import org.apache.commons.lang3.StringUtils;

import at.fhjoanneum.ippr.persistence.builder.Builder;
import at.fhjoanneum.ippr.persistence.entities.model.subject.SubjectModelImpl;
import at.fhjoanneum.ippr.persistence.objects.model.enums.StateEventType;
import at.fhjoanneum.ippr.persistence.objects.model.enums.StateFunctionType;
import at.fhjoanneum.ippr.persistence.objects.model.state.State;
import at.fhjoanneum.ippr.persistence.objects.model.subject.SubjectModel;

public class StateBuilder implements Builder<State> {

  private String name;
  private SubjectModelImpl subjectModel;
  private StateFunctionType functionType;
  private StateEventType eventType;
  private String refinementClass;

  public StateBuilder name(final String name) {
    isNotBlank(name);
    this.name = name;
    return this;
  }

  public StateBuilder subjectModel(final SubjectModel subjectModel) {
    checkNotNull(subjectModel);
    checkArgument(subjectModel instanceof SubjectModelImpl);
    this.subjectModel = (SubjectModelImpl) subjectModel;
    return this;
  }

  public StateBuilder functionType(final StateFunctionType functionType) {
    checkNotNull(functionType);
    this.functionType = functionType;
    return this;
  }

  public StateBuilder eventType(final StateEventType eventType) {
    checkNotNull(eventType);
    this.eventType = eventType;
    return this;
  }

  public StateBuilder refinementClass(final String refinementClass) {
    isNotBlank(refinementClass);
    this.refinementClass = refinementClass;
    return this;
  }

  @Override
  public State build() {
    isNotBlank(name);
    checkNotNull(subjectModel);
    checkNotNull(functionType);
    if (StateFunctionType.REFINEMENT.equals(functionType)) {
      checkArgument(StateFunctionType.REFINEMENT.equals(functionType)
          && StringUtils.isNotBlank(refinementClass));
    }

    if (StringUtils.isBlank(refinementClass)) {
      return new StateImpl(name, subjectModel, functionType, eventType);
    }

    return new StateImpl(name, subjectModel, functionType, eventType, refinementClass);
  }

}
