package at.fhjoanneum.ippr.persistence.entities.model.businessobject;

import static at.fhjoanneum.ippr.persistence.builder.BuilderUtils.isNotBlank;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import com.google.common.collect.Lists;

import at.fhjoanneum.ippr.persistence.builder.Builder;
import at.fhjoanneum.ippr.persistence.entities.model.state.StateImpl;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.BusinessObjectModel;
import at.fhjoanneum.ippr.persistence.objects.model.state.State;

public class BusinessObjectModelBuilder implements Builder<BusinessObjectModel> {

  private String name;
  private final List<StateImpl> states = Lists.newArrayList();
  private BusinessObjectModelImpl parent;

  public BusinessObjectModelBuilder name(final String name) {
    isNotBlank(name);
    this.name = name;
    return this;
  }

  public BusinessObjectModelBuilder addToState(final State state) {
    checkNotNull(state);
    checkArgument(state instanceof StateImpl);
    states.add((StateImpl) state);
    return this;
  }

  public BusinessObjectModelBuilder parent(final BusinessObjectModel parent) {
    checkNotNull(parent);
    checkArgument(parent instanceof BusinessObjectModelImpl);
    this.parent = (BusinessObjectModelImpl) parent;
    return this;
  }

  @Override
  public BusinessObjectModel build() {
    isNotBlank(name);

    if (parent == null) {
      return new BusinessObjectModelImpl(name, states);
    } else {
      return new BusinessObjectModelImpl(name, states, parent);
    }
  }
}
