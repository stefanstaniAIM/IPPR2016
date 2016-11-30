package at.fhjoanneum.ippr.processengine.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import at.fhjoanneum.ippr.persistence.entities.engine.state.SubjectStateImpl;

public interface SubjectStateRepository extends CrudRepository<SubjectStateImpl, Long> {

  @Query(value = "SELECT * FROM subject_state WHERE pi_id = :piId", nativeQuery = true)
  List<SubjectStateImpl> getSubjectStatesOfProcessInstance(@Param("piId") Long piId);
}
