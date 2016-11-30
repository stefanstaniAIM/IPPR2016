package at.fhjoanneum.ippr.processengine.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import at.fhjoanneum.ippr.persistence.entities.model.state.StateImpl;

@Repository
public interface StateRepository extends CrudRepository<StateImpl, Long> {

  @Query(value = "SELECT STATE.* FROM STATE WHERE event_type = 'START' and sm_id = :smId",
      nativeQuery = true)
  public StateImpl getStartStateOfSubject(@Param("smId") Long smId);
}
