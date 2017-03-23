package at.fhjoanneum.ippr.communicator.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import at.fhjoanneum.ippr.communicator.persistence.entities.config.OutboundConfigurationAssignement;

@Repository
public interface OutboundConfigurationAssignementRepository
    extends CrudRepository<OutboundConfigurationAssignement, Long> {

  OutboundConfigurationAssignement findByMessageFlowId(Long messageFlowId);
}
