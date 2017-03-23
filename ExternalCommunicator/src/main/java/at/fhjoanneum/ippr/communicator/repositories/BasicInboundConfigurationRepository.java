package at.fhjoanneum.ippr.communicator.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import at.fhjoanneum.ippr.communicator.persistence.entities.basic.inbound.BasicInboundConfigurationImpl;

@Repository
public interface BasicInboundConfigurationRepository
    extends CrudRepository<BasicInboundConfigurationImpl, Long> {

}
