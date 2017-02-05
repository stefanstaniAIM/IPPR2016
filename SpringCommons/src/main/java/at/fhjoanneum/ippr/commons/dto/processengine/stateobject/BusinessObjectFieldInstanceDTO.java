package at.fhjoanneum.ippr.commons.dto.processengine.stateobject;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class BusinessObjectFieldInstanceDTO implements Serializable {

  private static final long serialVersionUID = 1716611847318482348L;

  private Long bofmId;
  private String value;

  public BusinessObjectFieldInstanceDTO() {}

  public BusinessObjectFieldInstanceDTO(final Long bofmId, final String value) {
    this.bofmId = bofmId;
    this.value = value;
  }

  public Long getBofmId() {
    return bofmId;
  }

  public String getValue() {
    return StringUtils.isNotBlank(value) ? value : StringUtils.EMPTY;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("bofmId", bofmId)
        .append("value", value).toString();
  }
}
