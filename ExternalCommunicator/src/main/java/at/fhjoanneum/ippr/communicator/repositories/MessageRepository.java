package at.fhjoanneum.ippr.communicator.repositories;

import org.springframework.data.repository.CrudRepository;

import at.fhjoanneum.ippr.communicator.persistence.entities.messageflow.MessageImpl;

public interface MessageRepository extends CrudRepository<MessageImpl, Long> {

}
