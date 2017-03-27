package at.fhjoanneum.ippr.communicator.repositories;

import org.springframework.data.repository.CrudRepository;

import at.fhjoanneum.ippr.communicator.persistence.entities.submission.ReceiveSubmission;

public interface ReceiveSubmissionRepository extends CrudRepository<ReceiveSubmission, Long> {

}
