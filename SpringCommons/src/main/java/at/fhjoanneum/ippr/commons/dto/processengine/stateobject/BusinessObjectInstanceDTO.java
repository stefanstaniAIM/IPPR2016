package at.fhjoanneum.ippr.commons.dto.processengine.stateobject;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class BusinessObjectInstanceDTO implements Serializable {

  private static final long serialVersionUID = 1364364841800130590L;

  private Long bomId;

  private List<BusinessObjectFieldInstanceDTO> fields;

  public BusinessObjectInstanceDTO() {}

  public Long getBomId() {
    return bomId;
  }

  public List<BusinessObjectFieldInstanceDTO> getFields() {
    return fields;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("bomId", bomId)
        .append("fields", fields).toString();
  }
}
