package at.fhjoanneum.ippr.processengine.repositories;

import org.springframework.data.repository.CrudRepository;

import at.fhjoanneum.ippr.persistence.entities.model.process.ProcessModelImpl;

public interface ProcessModelRepository extends CrudRepository<ProcessModelImpl, Long> {

}
