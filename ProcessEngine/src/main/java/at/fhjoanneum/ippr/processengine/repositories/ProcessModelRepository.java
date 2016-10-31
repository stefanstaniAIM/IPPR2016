package at.fhjoanneum.ippr.processengine.repositories;

import at.fhjoanneum.ippr.persistence.objects.model.process.ProcessModel;

public interface ProcessModelRepository {
	void save(ProcessModel processModel);
}
