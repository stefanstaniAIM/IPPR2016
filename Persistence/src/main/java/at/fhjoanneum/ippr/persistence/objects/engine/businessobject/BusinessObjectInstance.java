package at.fhjoanneum.ippr.persistence.objects.engine.businessobject;

import java.util.List;
import java.util.Optional;

import at.fhjoanneum.ippr.persistence.objects.engine.process.ProcessInstance;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.BusinessObjectModel;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.field.BusinessObjectFieldModel;

public interface BusinessObjectInstance {

  Long getBoiId();

  ProcessInstance getProcessInstance();

  BusinessObjectModel getBusinessObjectModel();

  List<BusinessObjectFieldInstance> getBusinessObjectFieldInstances();

  Optional<BusinessObjectFieldInstance> getBusinessObjectFieldInstanceOfFieldModel(
      BusinessObjectFieldModel fieldModel);
}
