package at.fhjoanneum.ippr.processengine.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import at.fhjoanneum.ippr.persistence.entities.model.messageflow.MessageFlowImpl;

@Repository
public interface MessageFlowRepository extends CrudRepository<MessageFlowImpl, Long> {

}
