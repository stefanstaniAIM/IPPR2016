package at.fhjoanneum.ippr.persistence.entities.model.businessobject;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

import at.fhjoanneum.ippr.persistence.entities.model.businessobject.field.BusinessObjectFieldModelImpl;
import at.fhjoanneum.ippr.persistence.entities.model.state.StateImpl;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.BusinessObjectModel;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.field.BusinessObjectFieldModel;
import at.fhjoanneum.ippr.persistence.objects.model.state.State;

@Entity(name = "BUSINESS_OBJECT_MODEL")
public class BusinessObjectModelImpl implements BusinessObjectModel, Serializable {

  private static final long serialVersionUID = 5225641232293868945L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long bomId;

  @Column
  @NotBlank
  @Size(min = 1, max = 100)
  private String name;

  @ManyToMany(mappedBy = "businessObjectModels")
  private List<StateImpl> states;

  @OneToMany(mappedBy = "businessObjectModel")
  private List<BusinessObjectFieldModelImpl> businessObjectFields;

  BusinessObjectModelImpl() {}

  BusinessObjectModelImpl(final String name, final List<StateImpl> states) {
    this.name = name;
    this.states = states;
  }

  @Override
  public Long getBomId() {
    return bomId;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public List<BusinessObjectFieldModel> getBusinessObjectFieldModels() {
    return ImmutableList.copyOf(businessObjectFields);
  }

  @Override
  public List<State> getStates() {
    return ImmutableList.copyOf(states);
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null) {
      return false;
    }
    if (!BusinessObjectModel.class.isAssignableFrom(obj.getClass())) {
      return false;
    }
    final BusinessObjectModel other = (BusinessObjectModel) obj;
    if ((this.bomId == null) ? (other.getBomId() != null) : !this.bomId.equals(other.getBomId())) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(bomId);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("bomId", bomId)
        .append("name", name).toString();
  }
}
