package at.fhjoanneum.ippr.communicator.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import at.fhjoanneum.ippr.communicator.persistence.entities.config.OutboundConfigurationMap;

@Repository
public interface OutboundConfigurationMapRepository
    extends CrudRepository<OutboundConfigurationMap, Long> {

  OutboundConfigurationMap findByMessageFlowId(Long messageFlowId);
}
