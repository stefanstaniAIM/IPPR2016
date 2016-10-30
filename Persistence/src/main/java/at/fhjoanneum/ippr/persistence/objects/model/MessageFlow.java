package at.fhjoanneum.ippr.persistence.objects.model;

public interface MessageFlow {
	Long getMfId();

	SubjectModel getSender();

	SubjectModel getReceiver();
}
