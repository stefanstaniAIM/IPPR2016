package at.fhjoanneum.ippr.persistence.objects.model;

public interface Transition {
	Long getTId();

	State getFromState();

	State getToState();
}
