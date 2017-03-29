package at.fhjoanneum.ippr.communicator.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import at.fhjoanneum.ippr.communicator.persistence.entities.submission.ReceiveSubmission;

@Repository
public interface ReceiveSubmissionRepository extends CrudRepository<ReceiveSubmission, Long> {

  @Query(
      value = "select * from receive_submission where submission_state = 'TO_RECEIVE' and SUBSTRING_INDEX(transfer_id, '-', 2) = :transferId ",
      nativeQuery = true)
  ReceiveSubmission findByTransferId(@Param("transferId") String transferId);

  @Query(
      value = "select * from receive_submission where submission_state = 'TO_RECEIVE' and inbound_configuration_id = :configId order by created_at asc LIMIT 1",
      nativeQuery = true)
  ReceiveSubmission findByConfigId(@Param("configId") Long configId);

  @Query(
      value = "select * from receive_submission where SUBSTRING_INDEX(transfer_id, '-', 1) = :piId and submission_state = 'TO_RECEIVE'",
      nativeQuery = true)
  Set<ReceiveSubmission> findAllOfProcessInstance(@Param("piId") String piId);
}
