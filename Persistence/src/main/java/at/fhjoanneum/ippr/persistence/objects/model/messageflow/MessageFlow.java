package at.fhjoanneum.ippr.persistence.objects.model.messageflow;

import java.util.List;

import at.fhjoanneum.ippr.persistence.objects.model.businessobject.BusinessObjectModel;
import at.fhjoanneum.ippr.persistence.objects.model.subject.SubjectModel;

public interface MessageFlow {
  Long getMfId();

  SubjectModel getSender();

  SubjectModel getReceiver();

  List<BusinessObjectModel> getBusinessObjectModels();
}
