package at.fhjoanneum.ippr.processengine.repositories;

import org.springframework.data.repository.CrudRepository;

import at.fhjoanneum.ippr.persistence.entities.engine.state.SubjectStateImpl;

public interface SubjectStateRepository extends CrudRepository<SubjectStateImpl, Long> {

}
