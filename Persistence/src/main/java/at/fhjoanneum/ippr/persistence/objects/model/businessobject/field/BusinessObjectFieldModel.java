package at.fhjoanneum.ippr.persistence.objects.model.businessobject.field;

import at.fhjoanneum.ippr.persistence.objects.model.businessobject.BusinessObjectModel;
import at.fhjoanneum.ippr.persistence.objects.model.enums.FieldType;

public interface BusinessObjectFieldModel {

  Long getBofmId();

  String getFieldName();

  BusinessObjectModel getBusinessObjectModel();

  FieldType getFieldType();

  int getPosition();

  int getIndent();

  String getDefaultValue();
}
