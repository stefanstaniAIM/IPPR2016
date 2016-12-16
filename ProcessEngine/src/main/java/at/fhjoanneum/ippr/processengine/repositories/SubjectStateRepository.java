package at.fhjoanneum.ippr.processengine.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import at.fhjoanneum.ippr.persistence.entities.engine.state.SubjectStateImpl;

public interface SubjectStateRepository extends CrudRepository<SubjectStateImpl, Long> {

  @Query(value = "SELECT * FROM subject_state WHERE pi_id = :piId", nativeQuery = true)
  List<SubjectStateImpl> getSubjectStatesOfProcessInstance(@Param("piId") Long piId);

  @Query(
      value = "SELECT ss.* FROM ippr.SUBJECT_STATE ss " + "JOIN ippr.SUBJECT s ON s.s_id = ss.s_id "
          + "JOIN ippr.PROCESS_SUBJECT_INSTANCE_MAP psim ON psim.s_id = s.s_id "
          + "WHERE psim.pi_id = :piId AND s.user_id = :userId",
      nativeQuery = true)
  SubjectStateImpl getSubjectStateOfUserInProcessInstance(@Param("piId") Long piId,
      @Param("userId") Long userId);
}
