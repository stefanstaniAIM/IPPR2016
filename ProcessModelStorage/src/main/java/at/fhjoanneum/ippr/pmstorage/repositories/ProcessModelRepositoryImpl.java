package at.fhjoanneum.ippr.pmstorage.repositories;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import at.fhjoanneum.ippr.persistence.objects.model.process.ProcessModel;

@Repository
public class ProcessModelRepositoryImpl implements ProcessModelRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional
	public void save(final ProcessModel processModel) {
		entityManager.persist(processModel);
	}

}