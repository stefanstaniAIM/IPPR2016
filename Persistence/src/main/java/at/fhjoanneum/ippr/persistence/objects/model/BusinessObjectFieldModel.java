package at.fhjoanneum.ippr.persistence.objects.model;

import at.fhjoanneum.ippr.persistence.objects.model.enums.BusinessObjectFieldType;

public interface BusinessObjectFieldModel {

	Long getBofmId();

	String getFieldName();

	BusinessObjectFieldType getFieldType();
}
