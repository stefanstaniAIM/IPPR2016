package at.fhjoanneum.ippr.persistence.objects.engine.businessobject;

import at.fhjoanneum.ippr.persistence.objects.model.businessobject.field.BusinessObjectFieldModel;

public interface BusinessObjectFieldInstance {

  Long getBofiId();

  String getValue();

  void setValue(String value);

  BusinessObjectInstance getBusinessObjectInstance();

  BusinessObjectFieldModel getBusinessObjectFieldModel();
}
