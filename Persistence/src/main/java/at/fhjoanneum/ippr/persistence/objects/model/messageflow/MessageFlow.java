package at.fhjoanneum.ippr.persistence.objects.model.messageflow;

import at.fhjoanneum.ippr.persistence.objects.model.subject.SubjectModel;

public interface MessageFlow {
	Long getMfId();

	SubjectModel getSender();

	SubjectModel getReceiver();
}
