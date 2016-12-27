package at.fhjoanneum.ippr.persistence.entities.engine.businessobject;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import at.fhjoanneum.ippr.persistence.builder.Builder;
import at.fhjoanneum.ippr.persistence.entities.engine.process.ProcessInstanceImpl;
import at.fhjoanneum.ippr.persistence.entities.model.businessobject.BusinessObjectModelImpl;
import at.fhjoanneum.ippr.persistence.objects.engine.businessobject.BusinessObjectInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.process.ProcessInstance;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.BusinessObjectModel;

public class BusinessObjectInstanceBuilder implements Builder<BusinessObjectInstance> {

  private ProcessInstanceImpl processInstance;
  private BusinessObjectModelImpl businessObjectModel;

  public BusinessObjectInstanceBuilder processInstance(final ProcessInstance processInstance) {
    checkNotNull(processInstance);
    checkArgument(processInstance instanceof ProcessInstanceImpl);
    this.processInstance = (ProcessInstanceImpl) processInstance;
    return this;
  }

  public BusinessObjectInstanceBuilder businessObjectModel(
      final BusinessObjectModel businessObjectModel) {
    checkNotNull(businessObjectModel);
    checkArgument(businessObjectModel instanceof BusinessObjectModelImpl);
    this.businessObjectModel = (BusinessObjectModelImpl) businessObjectModel;
    return this;
  }

  @Override
  public BusinessObjectInstance build() {
    checkNotNull(processInstance);
    checkNotNull(businessObjectModel);

    return new BusinessObjectInstanceImpl(processInstance, businessObjectModel);
  }

}
