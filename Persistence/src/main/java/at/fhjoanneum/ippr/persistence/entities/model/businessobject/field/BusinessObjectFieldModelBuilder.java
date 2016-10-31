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

	public BusinessObjectFieldModelBuilder fieldName(final String name) {
		isNotBlank(name);
		this.name = name;
		return this;
	}

	public BusinessObjectFieldModelBuilder businessObjectModel(final BusinessObjectModel businessObjectModel) {
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

	@Override
	public BusinessObjectFieldModel build() {
		isNotBlank(name);
		checkNotNull(businessObjectModel);
		checkNotNull(fieldType);

		return new BusinessObjectFieldModelImpl(name, businessObjectModel, fieldType);
	}

}
