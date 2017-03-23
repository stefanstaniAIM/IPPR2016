package at.fhjoanneum.ippr.communicator.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import at.fhjoanneum.ippr.communicator.persistence.entities.basic.inbound.BasicInboundConfigurationImpl;

@Repository
public interface BasicInboundConfigurationRepository
    extends CrudRepository<BasicInboundConfigurationImpl, Long> {

  @Query(value = "select bic.* from basic_inbound_configuration bic "
      + "join basic_inbound_configuration_map bicm on bicm.basic_configuration_id = bic.id "
      + "where config_key = :key and value = :endpoint", nativeQuery = true)
  BasicInboundConfigurationImpl findByEndpoint(@Param("key") String key,
      @Param("endpoint") String endpoint);
}
