package at.fhjoanneum.ippr.communicator.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import at.fhjoanneum.ippr.communicator.persistence.entities.basic.inbound.rest.RestInboundConfigurationImpl;
import at.fhjoanneum.ippr.communicator.persistence.objects.basic.inbound.RestInboundConfiguration;

@Repository
public interface RestInboundConfigurationRepository
    extends CrudRepository<RestInboundConfigurationImpl, Long> {

  Optional<RestInboundConfiguration> findByEndpoint(String endpoint);
}
