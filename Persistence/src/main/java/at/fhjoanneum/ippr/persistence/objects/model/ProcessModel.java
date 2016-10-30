package at.fhjoanneum.ippr.persistence.objects.model;

import java.time.LocalDateTime;
import java.util.Set;

import at.fhjoanneum.ippr.persistence.objects.model.enums.ProcessModelState;

public interface ProcessModel {

	Long getPmId();

	String getName();

	String getDescription();

	LocalDateTime createdAt();

	Set<SubjectModel> getSubjectModels();

	void setState(ProcessModelState state);
}
