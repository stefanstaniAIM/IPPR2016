package at.fhjoanneum.ippr.persistence.entities.model;

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

import at.fhjoanneum.ippr.persistence.objects.model.BusinessObjectFieldModel;
import at.fhjoanneum.ippr.persistence.objects.model.BusinessObjectFieldModelPermission;
import at.fhjoanneum.ippr.persistence.objects.model.State;
import at.fhjoanneum.ippr.persistence.objects.model.enums.BusinessObjectFieldPermission;

@Entity(name = "BUSINESS_OBJECT_FIELD_MODEL_PERMISSION")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "s_id", "bofm_id" }))
public class BusinessObjectFieldModelPermissionImpl implements BusinessObjectFieldModelPermission, Serializable {

	private static final long serialVersionUID = 3309800374803885755L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long bofmpId;

	@ManyToOne
	@JoinColumn(name = "s_id")
	private StateImpl state;

	@ManyToOne
	@JoinColumn(name = "bofm_id")
	private BusinessObjectFieldModelImpl businessObjectFieldModel;

	@Column
	private final boolean mandatory = false;

	@Column
	@NotNull
	@Enumerated(EnumType.STRING)
	private BusinessObjectFieldPermission permission;

	@Override
	public Long getBofmpId() {
		return bofmpId;
	}

	@Override
	public State getState() {
		return state;
	}

	@Override
	public BusinessObjectFieldModel getBusinessObjectFieldModel() {
		return businessObjectFieldModel;
	}
}
