package at.fhjoanneum.ippr.persistence.objects.model.messageflow;

import java.util.List;
import java.util.Optional;

import at.fhjoanneum.ippr.persistence.objects.model.businessobject.BusinessObjectModel;
import at.fhjoanneum.ippr.persistence.objects.model.process.ProcessModel;
import at.fhjoanneum.ippr.persistence.objects.model.subject.SubjectModel;

public interface MessageFlow {
  Long getMfId();

  SubjectModel getSender();

  SubjectModel getReceiver();

  List<BusinessObjectModel> getBusinessObjectModels();

  Optional<ProcessModel> getAssignedProcessModel();
}
