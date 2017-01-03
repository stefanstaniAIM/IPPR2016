package at.fhjoanneum.ippr.processengine.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import at.fhjoanneum.ippr.persistence.entities.engine.subject.SubjectImpl;

@Repository
public interface SubjectRepository extends PagingAndSortingRepository<SubjectImpl, Long> {

  @Query(value = "SELECT s.* FROM subject s "
      + "JOIN process_subject_instance_map psi ON psi.s_id = s.s_id "
      + "WHERE psi.pi_id = :piId AND s.user_id = :uId", nativeQuery = true)
  public SubjectImpl getSubjectForUserInProcess(@Param("piId") Long piId,
      @Param("uId") Long userId);

  @Query(value = "SELECT s.* FROM subject s "
      + "JOIN process_subject_instance_map psi ON psi.s_id = s.s_id "
      + "WHERE psi.pi_id = :piId AND s.sm_id = :smId", nativeQuery = true)
  public SubjectImpl getSubjectForSubjectModelInProcess(@Param("piId") Long piId,
      @Param("smId") Long smId);
}
