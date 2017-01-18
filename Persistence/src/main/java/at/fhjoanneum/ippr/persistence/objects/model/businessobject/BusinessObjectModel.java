package at.fhjoanneum.ippr.persistence.objects.model.businessobject;

import java.util.List;

import at.fhjoanneum.ippr.persistence.objects.model.businessobject.field.BusinessObjectFieldModel;
import at.fhjoanneum.ippr.persistence.objects.model.state.State;

public interface BusinessObjectModel {

  Long getBomId();

  String getName();

  List<BusinessObjectFieldModel> getBusinessObjectFieldModels();

  List<State> getStates();

  BusinessObjectModel getParent();

  boolean hasParent();

  List<BusinessObjectModel> getChildren();

  List<BusinessObjectModel> flattened();
}
