package at.fhjoanneum.ippr.pmstorage.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import at.fhjoanneum.ippr.persistence.entities.model.process.ProcessModelImpl;

@Repository
public interface ProcessModelRepository extends PagingAndSortingRepository<ProcessModelImpl, Long> {

  @Query(value = "SELECT p FROM PROCESS_MODEL p WHERE p.state = 'ACTIVE'")
  public Page<ProcessModelImpl> findActiveProcesses(Pageable pageable);

  @Query(
      value = "SELECT p FROM PROCESS_MODEL p INNER JOIN p.starterSubject starter WHERE p.state = 'ACTIVE' AND starter.assignedGroup IN :groups")
  public Page<ProcessModelImpl> findActiveProcessesToStart(Pageable pageable,
      @Param("groups") List<String> groups);
}
