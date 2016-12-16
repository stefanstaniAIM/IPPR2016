package at.fhjoanneum.ippr.processengine.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import at.fhjoanneum.ippr.persistence.entities.model.businessobject.permission.BusinessObjectFieldPermissionImpl;

@Repository
public interface BusinessObjectFieldPermissionRepository
    extends CrudRepository<BusinessObjectFieldPermissionImpl, Long> {

  @Query(
      value = "SELECT * FROM BUSINESS_OBJECT_FIELD_PERMISSION bofp WHERE bofm_id = :bofmId AND s_id = :sId",
      nativeQuery = true)
  BusinessObjectFieldPermissionImpl getBusinessObjectFieldPermissionInState(
      @Param("bofmId") Long bofmId, @Param("sId") Long sId);
}
