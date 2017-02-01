package at.fhjoanneum.ippr.persistence.entities.model.businessobject.field;

import static at.fhjoanneum.ippr.persistence.builder.BuilderUtils.isNotBlank;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import at.fhjoanneum.ippr.persistence.builder.Builder;
import at.fhjoanneum.ippr.persistence.entities.model.businessobject.BusinessObjectModelImpl;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.BusinessObjectModel;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.field.BusinessObjectFieldModel;
import at.fhjoanneum.ippr.persistence.objects.model.enums.FieldType;

public class BusinessObjectFieldModelBuilder implements Builder<BusinessObjectFieldModel> {

  private String name;
  private BusinessObjectModelImpl businessObjectModel;
  private FieldType fieldType;
  private String defaultValue;
  private int position = -1;
  private int indent = -1;

  public BusinessObjectFieldModelBuilder fieldName(final String name) {
    isNotBlank(name);
    this.name = name;
    return this;
  }

  public BusinessObjectFieldModelBuilder businessObjectModel(
      final BusinessObjectModel businessObjectModel) {
    checkNotNull(businessObjectModel);
    checkArgument(businessObjectModel instanceof BusinessObjectModelImpl);
    this.businessObjectModel = (BusinessObjectModelImpl) businessObjectModel;
    return this;
  }

  public BusinessObjectFieldModelBuilder fieldType(final FieldType fieldType) {
    checkNotNull(fieldType);
    this.fieldType = fieldType;
    return this;
  }

  public BusinessObjectFieldModelBuilder defaultValue(final String defaultValue) {
    isNotBlank(defaultValue);
    this.defaultValue = defaultValue;
    return this;
  }


  public BusinessObjectFieldModelBuilder position(final int position) {
    checkArgument(position >= 0);
    this.position = position;
    return this;
  }

  public BusinessObjectFieldModelBuilder indent(final int indent) {
    checkArgument(indent >= 0);
    this.indent = indent;
    return this;
  }

  @Override
  public BusinessObjectFieldModel build() {
    isNotBlank(name);
    checkNotNull(businessObjectModel);
    checkNotNull(fieldType);
    checkArgument(position >= 0);

    if (indent >= 0) {
      return new BusinessObjectFieldModelImpl(name, businessObjectModel, fieldType, defaultValue,
          position, indent);
    }
    return new BusinessObjectFieldModelImpl(name, businessObjectModel, fieldType, defaultValue,
        position);
  }

}
