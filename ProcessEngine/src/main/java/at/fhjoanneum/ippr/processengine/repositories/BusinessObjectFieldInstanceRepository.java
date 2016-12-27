package at.fhjoanneum.ippr.processengine.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import at.fhjoanneum.ippr.persistence.entities.engine.businessobject.field.BusinessObjectFieldInstanceImpl;

@Repository
public interface BusinessObjectFieldInstanceRepository
    extends JpaRepository<BusinessObjectFieldInstanceImpl, Long> {

  @Query(value = "SELECT * FROM BUSINESS_OBJECT_FIELD_INSTANCE bofi "
      + "JOIN BUSINESS_OBJECT_INSTANCE boi ON boi.boi_id = bofi.boi_id "
      + "WHERE bofi.bofm_id = :bofmId AND boi.pi_id = :piId", nativeQuery = true)
  BusinessObjectFieldInstanceImpl getBusinessObjectFieldInstanceForModelInProcess(
      @Param("piId") Long piId, @Param("bofmId") Long bofmId);

}
