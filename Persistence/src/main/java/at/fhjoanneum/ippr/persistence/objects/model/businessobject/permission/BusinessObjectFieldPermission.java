package at.fhjoanneum.ippr.persistence.objects.model.businessobject.permission;

import at.fhjoanneum.ippr.persistence.objects.model.businessobject.field.BusinessObjectFieldModel;
import at.fhjoanneum.ippr.persistence.objects.model.enums.FieldPermission;
import at.fhjoanneum.ippr.persistence.objects.model.state.State;

public interface BusinessObjectFieldPermission {

	Long getBofpId();

	State getState();

	BusinessObjectFieldModel getBusinessObjectFieldModel();

	boolean isMandatory();

	FieldPermission getPermission();
}
