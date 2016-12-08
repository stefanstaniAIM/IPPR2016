package at.fhjoanneum.ippr.processengine.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import at.fhjoanneum.ippr.persistence.entities.engine.process.ProcessInstanceImpl;
import at.fhjoanneum.ippr.persistence.objects.engine.enums.ProcessInstanceState;

@Repository
public interface ProcessInstanceRepository
    extends PagingAndSortingRepository<ProcessInstanceImpl, Long> {

  @Query(value = "SELECT * FROM PROCESS_INSTANCE p WHERE p.state = :state", nativeQuery = true)
  List<ProcessInstanceImpl> getProcessesWithState(@Param("state") String state);

  @Query(value = "SELECT COUNT(p.pi_id) FROM PROCESS_INSTANCE p WHERE p.state = :state",
      nativeQuery = true)
  Long getAmountOfProcesses(@Param("state") String state);

  @Query(
      value = "SELECT count(p.pi_id) FROM PROCESS_INSTANCE p JOIN PROCESS_SUBJECT_INSTANCE_MAP psm on psm.pi_id = p.pi_id "
          + "JOIN SUBJECT s on s.s_id = psm.s_id WHERE p.state = :state and s.user_id = :userId",
      nativeQuery = true)
  Long getAmountOfProcessesPerUser(@Param("state") String state, @Param("userId") Long userId);

  @Query(value = "SELECT p FROM PROCESS_INSTANCE p WHERE p.state = :state", nativeQuery = false)
  Page<ProcessInstanceImpl> getProcessesInfoOfState(Pageable pageable,
      @Param("state") ProcessInstanceState state);
}
