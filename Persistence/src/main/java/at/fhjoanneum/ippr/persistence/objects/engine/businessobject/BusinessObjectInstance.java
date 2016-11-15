package at.fhjoanneum.ippr.persistence.objects.engine.businessobject;

import java.util.List;

import at.fhjoanneum.ippr.persistence.objects.engine.process.ProcessInstance;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.BusinessObjectModel;

public interface BusinessObjectInstance {

  Long getBoiId();

  ProcessInstance getProcessInstance();

  BusinessObjectModel getBusinessObjectModel();

  List<BusinessObjectFieldInstance> getBusinessObjectFieldInstances();
}
