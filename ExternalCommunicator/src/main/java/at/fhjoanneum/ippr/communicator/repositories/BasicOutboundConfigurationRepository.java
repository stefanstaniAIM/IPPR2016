package at.fhjoanneum.ippr.communicator.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import at.fhjoanneum.ippr.communicator.persistence.entities.basic.outbound.BasicOutboundConfigurationImpl;

@Repository
public interface BasicOutboundConfigurationRepository
    extends CrudRepository<BasicOutboundConfigurationImpl, Long> {

}
