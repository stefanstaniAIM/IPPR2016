package at.fhjoanneum.ippr.persistence.objects.model;

import java.util.List;

public interface BusinessObjectModel {

	Long getBomId();

	String getName();

	List<BusinessObjectFieldModel> getBusinessObjectFieldModels();
}
