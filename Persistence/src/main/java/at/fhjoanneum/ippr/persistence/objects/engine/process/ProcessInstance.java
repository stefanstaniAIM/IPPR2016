package at.fhjoanneum.ippr.persistence.objects.engine.process;

import java.time.LocalDateTime;
import java.util.List;

import at.fhjoanneum.ippr.persistence.objects.engine.businessobject.BusinessObjectInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.enums.ProcessInstanceState;
import at.fhjoanneum.ippr.persistence.objects.engine.subject.Subject;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.BusinessObjectModel;
import at.fhjoanneum.ippr.persistence.objects.model.process.ProcessModel;

public interface ProcessInstance {

  Long getPiId();

  ProcessModel getProcessModel();

  ProcessInstanceState getState();

  void setState(ProcessInstanceState state);

  LocalDateTime getStartTime();

  LocalDateTime getEndTime();

  Long getStartUserId();

  void setEndTime();

  List<Subject> getSubjects();

  List<BusinessObjectInstance> getBusinessObjectInstances();

  boolean isBusinessObjectInstanceOfModelCreated(BusinessObjectModel businessObjectModel);

  boolean isStopped();
}
