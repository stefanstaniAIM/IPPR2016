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

  @Query(value = "SELECT pm.* , sender.subject_model_type " + "FROM subject_model_rule smr "
      + "JOIN subject_model sm ON sm.sm_id = smr.sm_id "
      + "JOIN process_model pm ON pm.starter_subject = sm.sm_id "
      + "JOIN state ON state.sm_id = sm.sm_id "
      + "LEFT JOIN message_flow mf ON mf.s_id = state.s_id "
      + "LEFT JOIN subject_model sender ON sender.sm_id = mf.sender "
      + "WHERE pm.state = 'ACTIVE' AND smr.name in :rules AND state.event_type = 'START' "
      + "AND sender.subject_model_type IS NULL", nativeQuery = true)
  public List<ProcessModelImpl> findActiveProcessesToStart(@Param("rules") List<String> rules);

  @Query(value = "SELECT * FROM process_model ORDER BY name ASC, version ASC", nativeQuery = true)
  public List<ProcessModelImpl> findAllOrderedByName();
}
