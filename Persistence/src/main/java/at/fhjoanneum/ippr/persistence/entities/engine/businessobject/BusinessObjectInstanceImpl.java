package at.fhjoanneum.ippr.persistence.entities.engine.businessobject;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.collect.Lists;

import at.fhjoanneum.ippr.persistence.entities.engine.businessobject.field.BusinessObjectFieldInstanceImpl;
import at.fhjoanneum.ippr.persistence.entities.engine.process.ProcessInstanceImpl;
import at.fhjoanneum.ippr.persistence.entities.model.businessobject.BusinessObjectModelImpl;
import at.fhjoanneum.ippr.persistence.objects.engine.businessobject.BusinessObjectFieldInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.businessobject.BusinessObjectInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.process.ProcessInstance;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.BusinessObjectModel;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.field.BusinessObjectFieldModel;

@Entity(name = "BUSINESS_OBJECT_INSTANCE")
public class BusinessObjectInstanceImpl implements BusinessObjectInstance, Serializable {

  private static final long serialVersionUID = 2347891528398055530L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long boiId;

  @ManyToOne
  @JoinColumn(name = "piId")
  private ProcessInstanceImpl processInstance;

  @ManyToOne
  @JoinColumn(name = "bomId")
  private BusinessObjectModelImpl businessObjectModel;

  @OneToMany(mappedBy = "businessObjectInstance")
  private List<BusinessObjectFieldInstanceImpl> businessObjectFieldInstances;

  BusinessObjectInstanceImpl() {}

  BusinessObjectInstanceImpl(final ProcessInstanceImpl processInstance,
      final BusinessObjectModelImpl businessObjectModel) {
    this.processInstance = processInstance;
    this.businessObjectModel = businessObjectModel;
  }

  @Override
  public Long getBoiId() {
    return boiId;
  }

  @Override
  public ProcessInstance getProcessInstance() {
    return processInstance;
  }

  @Override
  public BusinessObjectModel getBusinessObjectModel() {
    return businessObjectModel;
  }

  @Override
  public List<BusinessObjectFieldInstance> getBusinessObjectFieldInstances() {
    return Lists.newArrayList(businessObjectFieldInstances);
  }


  @Override
  public Optional<BusinessObjectFieldInstance> getBusinessObjectFieldInstanceOfFieldModel(
      final BusinessObjectFieldModel fieldModel) {
    Optional<BusinessObjectFieldInstance> opt = Optional.empty();

    for (final BusinessObjectFieldInstance fieldInstance : businessObjectFieldInstances) {
      if (fieldInstance.getBusinessObjectFieldModel().equals(fieldModel)) {
        opt = Optional.of(fieldInstance);
        break;
      }
    }
    return opt;
  }

  @Override
  public int hashCode() {
    return Objects.hash(boiId);
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final BusinessObjectInstanceImpl other = (BusinessObjectInstanceImpl) obj;
    if (boiId == null) {
      if (other.boiId != null) {
        return false;
      }
    } else if (!boiId.equals(other.boiId)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("boiId", boiId)
        .append("piId", processInstance.getPiId())
        .append("business object model", businessObjectModel).toString();
  }
}
