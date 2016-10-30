package at.fhjoanneum.ippr.persistence.entities.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

import at.fhjoanneum.ippr.persistence.objects.model.BusinessObjectFieldModel;
import at.fhjoanneum.ippr.persistence.objects.model.BusinessObjectModel;

@Entity(name = "BUSINESS_OBJECT_MODEL")
public class BusinessObjectModelImpl implements BusinessObjectModel, Serializable {

	private static final long serialVersionUID = 5225641232293868945L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long bomId;

	@Column
	@NotNull
	@Size(min = 1, max = 100)
	private String name;

	@OneToMany
	@JoinColumn(name = "bom_id")
	private List<BusinessObjectFieldModelImpl> businessObjectFields;

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
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("bomId", bomId).append("name", name)
				.toString();
	}
}
