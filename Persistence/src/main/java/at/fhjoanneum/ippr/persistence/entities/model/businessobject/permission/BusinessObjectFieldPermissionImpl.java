package at.fhjoanneum.ippr.persistence.entities.model.businessobject.permission;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.base.Objects;

import at.fhjoanneum.ippr.persistence.entities.model.businessobject.field.BusinessObjectFieldModelImpl;
import at.fhjoanneum.ippr.persistence.entities.model.state.StateImpl;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.field.BusinessObjectFieldModel;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.permission.BusinessObjectFieldPermission;
import at.fhjoanneum.ippr.persistence.objects.model.enums.FieldPermission;
import at.fhjoanneum.ippr.persistence.objects.model.state.State;

@Entity(name = "BUSINESS_OBJECT_FIELD_PERMISSION")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "s_id", "bofm_id" }))
public class BusinessObjectFieldPermissionImpl implements BusinessObjectFieldPermission, Serializable {

	private static final long serialVersionUID = 3309800374803885755L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long bofpId;

	@ManyToOne
	@JoinColumn(name = "s_id")
	private StateImpl state;

	@ManyToOne
	@JoinColumn(name = "bofm_id")
	private BusinessObjectFieldModelImpl businessObjectFieldModel;

	@Column
	private boolean mandatory = false;

	@Column
	@NotNull
	@Enumerated(EnumType.STRING)
	private FieldPermission permission;

	BusinessObjectFieldPermissionImpl() {
	}

	BusinessObjectFieldPermissionImpl(final StateImpl state,
			final BusinessObjectFieldModelImpl businessObjectFieldModel, final FieldPermission permission,
			final boolean mandatory) {
		this.state = state;
		this.businessObjectFieldModel = businessObjectFieldModel;
		this.permission = permission;
		this.mandatory = mandatory;
	}

	@Override
	public Long getBofpId() {
		return bofpId;
	}

	@Override
	public State getState() {
		return state;
	}

	@Override
	public BusinessObjectFieldModel getBusinessObjectFieldModel() {
		return businessObjectFieldModel;
	}

	@Override
	public boolean isMandatory() {
		return mandatory;
	}

	@Override
	public FieldPermission getPermission() {
		return permission;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!BusinessObjectFieldPermissionImpl.class.isAssignableFrom(obj.getClass())) {
			return false;
		}
		final BusinessObjectFieldPermissionImpl other = (BusinessObjectFieldPermissionImpl) obj;
		if ((this.bofpId == null) ? (other.getBofpId() != null) : !this.bofpId.equals(other.getBofpId())) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(bofpId);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("bofpId", bofpId)
				.append("subject", state.getSubjectModel().getName()).append("state", state.getName())
				.append("permission", permission).toString();
	}
}
