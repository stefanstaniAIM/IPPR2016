package at.fhjoanneum.ippr.pmstorage.repositories;

import at.fhjoanneum.ippr.persistence.objects.model.process.ProcessModel;

public interface ProcessModelRepository {
	void save(ProcessModel processModel);
}
