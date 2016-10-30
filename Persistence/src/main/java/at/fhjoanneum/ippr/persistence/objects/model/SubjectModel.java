package at.fhjoanneum.ippr.persistence.objects.model;

import java.util.List;

public interface SubjectModel {

	Long getSmId();

	String getName();

	String getGroup();

	List<State> getStates();
}
