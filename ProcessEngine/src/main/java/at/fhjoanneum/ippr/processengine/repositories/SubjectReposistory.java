package at.fhjoanneum.ippr.processengine.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import at.fhjoanneum.ippr.persistence.entities.engine.subject.SubjectImpl;

@Repository
public interface SubjectReposistory extends PagingAndSortingRepository<SubjectImpl, Long> {

}
