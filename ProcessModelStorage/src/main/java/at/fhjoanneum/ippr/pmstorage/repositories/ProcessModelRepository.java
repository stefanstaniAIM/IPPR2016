package at.fhjoanneum.ippr.pmstorage.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import at.fhjoanneum.ippr.persistence.entities.model.process.ProcessModelImpl;

@Repository
public interface ProcessModelRepository extends PagingAndSortingRepository<ProcessModelImpl, Long> {

  @Query(value = "SELECT p FROM PROCESS_MODEL p WHERE p.state = 'ACTIVE'")
  public List<ProcessModelImpl> findActiveProcesses();

  @Query(
      value = "SELECT pm.* FROM subject_model_rule smr "
          + "JOIN subject_model sm ON sm.sm_id = smr.sm_id "
          + "JOIN process_model pm ON pm.starter_subject = sm.sm_id WHERE smr.name in :rules AND pm.state = 'ACTIVE'",
      nativeQuery = true)
  public List<ProcessModelImpl> findActiveProcessesToStart(@Param("rules") List<String> rules);
}
