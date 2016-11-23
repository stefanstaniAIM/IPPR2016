package at.fhjoanneum.ippr.processengine.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import at.fhjoanneum.ippr.persistence.entities.model.subject.SubjectModelImpl;

@Repository
public interface SubjectModelRepository extends CrudRepository<SubjectModelImpl, Long> {

}
