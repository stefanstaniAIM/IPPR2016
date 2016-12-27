package at.fhjoanneum.ippr.persistence.entities.engine.businessobject.field;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import at.fhjoanneum.ippr.persistence.builder.Builder;
import at.fhjoanneum.ippr.persistence.entities.engine.businessobject.BusinessObjectInstanceImpl;
import at.fhjoanneum.ippr.persistence.entities.model.businessobject.field.BusinessObjectFieldModelImpl;
import at.fhjoanneum.ippr.persistence.objects.engine.businessobject.BusinessObjectFieldInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.businessobject.BusinessObjectInstance;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.field.BusinessObjectFieldModel;

public class BusinessObjectFieldInstanceBuilder implements Builder<BusinessObjectFieldInstance> {

  private BusinessObjectInstanceImpl businessObjectInstance;
  private BusinessObjectFieldModelImpl businessObjectFieldModel;
  private String value;

  public BusinessObjectFieldInstanceBuilder businessObjectInstance(
      final BusinessObjectInstance businessObjectInstance) {
    checkNotNull(businessObjectInstance);
    checkArgument(businessObjectInstance instanceof BusinessObjectInstanceImpl);
    this.businessObjectInstance = (BusinessObjectInstanceImpl) businessObjectInstance;
    return this;
  }

  public BusinessObjectFieldInstanceBuilder businessObjectFieldModel(
      final BusinessObjectFieldModel businessObjectFieldModel) {
    checkNotNull(businessObjectFieldModel);
    checkArgument(businessObjectFieldModel instanceof BusinessObjectFieldModelImpl);
    this.businessObjectFieldModel = (BusinessObjectFieldModelImpl) businessObjectFieldModel;
    return this;
  }

  public BusinessObjectFieldInstanceBuilder value(final String value) {
    checkNotNull(value);
    this.value = value;
    return this;
  }

  @Override
  public BusinessObjectFieldInstance build() {
    checkNotNull(businessObjectFieldModel);
    checkNotNull(businessObjectInstance);

    if (value == null) {
      return new BusinessObjectFieldInstanceImpl(businessObjectInstance, businessObjectFieldModel);
    }
    return new BusinessObjectFieldInstanceImpl(businessObjectInstance, businessObjectFieldModel,
        value);
  }

}
