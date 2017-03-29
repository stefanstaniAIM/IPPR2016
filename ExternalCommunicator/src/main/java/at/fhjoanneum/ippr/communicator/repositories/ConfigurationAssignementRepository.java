package at.fhjoanneum.ippr.communicator.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import at.fhjoanneum.ippr.communicator.persistence.entities.config.ConfigurationAssignement;

@Repository
public interface ConfigurationAssignementRepository
    extends CrudRepository<ConfigurationAssignement, Long> {

  ConfigurationAssignement findByMessageFlowId(Long messageFlowId);

  @Query(
      value = "select * from configuration_assignment where INBOUND_CONFIG_ID = :inboundConfigId",
      nativeQuery = true)
  ConfigurationAssignement findByInboundConfiguration(
      @Param("inboundConfigId") Long inboundConfigId);
}
