package at.fhjoanneum.ippr.processengine.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import at.fhjoanneum.ippr.persistence.entities.engine.state.SubjectStateImpl;
import at.fhjoanneum.ippr.persistence.objects.engine.state.SubjectState;

public interface SubjectStateRepository extends CrudRepository<SubjectStateImpl, Long> {

  @Query(value = "SELECT * FROM subject_state WHERE pi_id = :piId", nativeQuery = true)
  List<SubjectStateImpl> getSubjectStates(@Param("piId") Long piId);

  @Query(value = "SELECT ss.* FROM SUBJECT_STATE ss JOIN SUBJECT s ON s.s_id = ss.s_id "
      + "JOIN PROCESS_SUBJECT_INSTANCE_MAP psim ON psim.s_id = s.s_id "
      + "WHERE psim.pi_id = :piId AND s.user_id = :userId", nativeQuery = true)
  SubjectStateImpl getSubjectStateOfUser(@Param("piId") Long piId, @Param("userId") Long userId);

  @Query(
      value = "SELECT ss.* FROM SUBJECT_STATE ss JOIN SUBJECT s ON s.s_id = ss.s_id "
          + "JOIN PROCESS_SUBJECT_INSTANCE_MAP psim ON psim.s_id = s.s_id "
          + "WHERE psim.pi_id = :piId AND s.user_id = :userId AND ss.sub_state IN ('TO_RECEIVE', 'RECEIVED')",
      nativeQuery = true)
  SubjectStateImpl getToReceiveSubjectStateOfUser(@Param("piId") Long piId,
      @Param("userId") Long userId);

  @Query(value = "SELECT ss FROM SUBJECT_STATE ss WHERE ss.timeoutActor IS NOT NULL",
      nativeQuery = false)
  List<SubjectState> getSubjectStatesWithTimeout();
}
