package at.fhjoanneum.ippr.persistence.entities.engine.businessobject.field;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import at.fhjoanneum.ippr.persistence.entities.engine.businessobject.BusinessObjectInstanceImpl;
import at.fhjoanneum.ippr.persistence.entities.model.businessobject.field.BusinessObjectFieldModelImpl;
import at.fhjoanneum.ippr.persistence.objects.engine.businessobject.BusinessObjectFieldInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.businessobject.BusinessObjectInstance;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.field.BusinessObjectFieldModel;

@Entity(name = "BUSINESS_OBJECT_FIELD_INSTANCE")
public class BusinessObjectFieldInstanceImpl implements BusinessObjectFieldInstance, Serializable {

  private static final long serialVersionUID = 3433121899600176278L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long bofiId;

  @ManyToOne
  @JoinColumn(name = "boiId")
  private BusinessObjectInstanceImpl businessObjectInstance;

  @ManyToOne
  @JoinColumn(name = "bofmId")
  private BusinessObjectFieldModelImpl businessObjectFieldModel;

  @Column
  private String value;

  BusinessObjectFieldInstanceImpl() {}

  BusinessObjectFieldInstanceImpl(final BusinessObjectInstanceImpl businessObjectInstance,
      final BusinessObjectFieldModelImpl businessObjectFieldModel) {
    this.businessObjectInstance = businessObjectInstance;
    this.businessObjectFieldModel = businessObjectFieldModel;
  }

  BusinessObjectFieldInstanceImpl(final BusinessObjectInstanceImpl businessObjectInstance,
      final BusinessObjectFieldModelImpl businessObjectFieldModel, final String value) {
    this(businessObjectInstance, businessObjectFieldModel);
    this.value = value;
  }


  @Override
  public String getValue() {
    return value;
  }

  @Override
  public void setValue(final String value) {
    this.value = value;
  }

  @Override
  public Long getBofiId() {
    return bofiId;
  }

  @Override
  public BusinessObjectInstance getBusinessObjectInstance() {
    return businessObjectInstance;
  }

  @Override
  public BusinessObjectFieldModel getBusinessObjectFieldModel() {
    return businessObjectFieldModel;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(bofiId);
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final BusinessObjectFieldInstanceImpl other = (BusinessObjectFieldInstanceImpl) obj;
    if (bofiId == null) {
      if (other.bofiId != null)
        return false;
    } else if (!bofiId.equals(other.bofiId))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("bofiId", bofiId)
        .append("value", value).append("field model", businessObjectFieldModel).toString();
  }
}
