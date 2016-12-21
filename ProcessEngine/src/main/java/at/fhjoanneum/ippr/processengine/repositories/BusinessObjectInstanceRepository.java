package at.fhjoanneum.ippr.processengine.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import at.fhjoanneum.ippr.persistence.entities.engine.businessobject.BusinessObjectInstanceImpl;

@Repository
public interface BusinessObjectInstanceRepository
    extends CrudRepository<BusinessObjectInstanceImpl, Long> {

  @Query(value = "SELECT * FROM BUSINESS_OBJECT_INSTANCE WHERE pi_id = :piId AND bom_id = :bomId",
      nativeQuery = true)
  BusinessObjectInstanceImpl getBusinessObjectInstanceOfModelInProcess(@Param("piId") Long piId,
      @Param("bomId") Long bomId);
}
