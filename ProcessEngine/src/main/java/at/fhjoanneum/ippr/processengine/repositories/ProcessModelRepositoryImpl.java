package at.fhjoanneum.ippr.processengine.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import at.fhjoanneum.ippr.persistence.entities.model.ProcessModelmpl;
import at.fhjoanneum.ippr.persistence.objects.model.ProcessModel;
import at.fhjoanneum.ippr.persistence.objects.model.enums.ProcessModelState;

@Repository
public class ProcessModelRepositoryImpl implements ProcessModelRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional
	public void save(final ProcessModel processModel) {
		// entityManager.persist(processModel);
		final Query createNativeQuery = entityManager.createNativeQuery("select * from process_model",
				ProcessModelmpl.class);
		final List<ProcessModel> resultList = createNativeQuery.getResultList();
		final ProcessModel pm = resultList.get(0);
		pm.setState(ProcessModelState.INACTIVE);
		entityManager.merge(pm);
	}

}