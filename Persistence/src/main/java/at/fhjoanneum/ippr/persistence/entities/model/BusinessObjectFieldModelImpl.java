package at.fhjoanneum.ippr.persistence.entities.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.base.Objects;

import at.fhjoanneum.ippr.persistence.objects.model.BusinessObjectFieldModel;
import at.fhjoanneum.ippr.persistence.objects.model.enums.BusinessObjectFieldType;

@Entity(name = "BUSINESS_OBJECT_FIELD_MODEL")
public class BusinessObjectFieldModelImpl implements BusinessObjectFieldModel, Serializable {

	private static final long serialVersionUID = -8651866037837204065L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long bofmId;

	@NotNull
	@Column
	@Size(min = 1, max = 100)
	private String fieldName;

	@Column
	@NotNull
	@Enumerated(EnumType.STRING)
	private BusinessObjectFieldType fieldType;

	@Override
	public Long getBofmId() {
		return bofmId;
	}

	@Override
	public String getFieldName() {
		return fieldName;
	}

	@Override
	public BusinessObjectFieldType getFieldType() {
		return fieldType;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!BusinessObjectFieldModel.class.isAssignableFrom(obj.getClass())) {
			return false;
		}
		final BusinessObjectFieldModel other = (BusinessObjectFieldModel) obj;
		if ((this.bofmId == null) ? (other.getBofmId() != null) : !this.bofmId.equals(other.getBofmId())) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(bofmId);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("bofmId", bofmId)
				.append("fieldName", fieldName).toString();
	}
}
