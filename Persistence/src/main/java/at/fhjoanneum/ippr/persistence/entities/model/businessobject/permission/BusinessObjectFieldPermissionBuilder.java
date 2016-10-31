package at.fhjoanneum.ippr.persistence.entities.model.businessobject.permission;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import at.fhjoanneum.ippr.persistence.builder.Builder;
import at.fhjoanneum.ippr.persistence.entities.model.businessobject.field.BusinessObjectFieldModelImpl;
import at.fhjoanneum.ippr.persistence.entities.model.state.StateImpl;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.field.BusinessObjectFieldModel;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.permission.BusinessObjectFieldPermission;
import at.fhjoanneum.ippr.persistence.objects.model.enums.FieldPermission;
import at.fhjoanneum.ippr.persistence.objects.model.state.State;

public class BusinessObjectFieldPermissionBuilder implements Builder<BusinessObjectFieldPermission> {

	private StateImpl state;
	private BusinessObjectFieldModelImpl businessObjectFieldModel;
	private boolean mandatory;
	private FieldPermission permission;

	public BusinessObjectFieldPermissionBuilder state(final State state) {
		checkNotNull(state);
		checkArgument(state instanceof StateImpl);
		this.state = (StateImpl) state;
		return this;
	}

	public BusinessObjectFieldPermissionBuilder businessObjectFieldModel(final BusinessObjectFieldModel field) {
		checkNotNull(field);
		checkArgument(field instanceof BusinessObjectFieldModelImpl);
		this.businessObjectFieldModel = (BusinessObjectFieldModelImpl) field;
		return this;
	}

	public BusinessObjectFieldPermissionBuilder permission(final FieldPermission permission) {
		checkNotNull(permission);
		this.permission = permission;
		return this;
	}

	public BusinessObjectFieldPermissionBuilder mandatory(final boolean mandatory) {
		this.mandatory = mandatory;
		return this;
	}

	@Override
	public BusinessObjectFieldPermission build() {
		checkNotNull(state);
		checkNotNull(businessObjectFieldModel);
		checkNotNull(permission);

		return new BusinessObjectFieldPermissionImpl(state, businessObjectFieldModel, permission, mandatory);
	}

}
