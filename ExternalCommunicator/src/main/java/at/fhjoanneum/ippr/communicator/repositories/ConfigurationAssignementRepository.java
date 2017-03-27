package at.fhjoanneum.ippr.communicator.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import at.fhjoanneum.ippr.communicator.persistence.entities.config.ConfigurationAssignement;

@Repository
public interface ConfigurationAssignementRepository
    extends CrudRepository<ConfigurationAssignement, Long> {

  ConfigurationAssignement findByMessageFlowId(Long messageFlowId);
}
