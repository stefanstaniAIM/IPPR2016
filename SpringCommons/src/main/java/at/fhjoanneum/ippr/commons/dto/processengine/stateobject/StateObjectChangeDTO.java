package at.fhjoanneum.ippr.commons.dto.processengine.stateobject;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@XmlRootElement
public class StateObjectChangeDTO implements Serializable {

  private static final long serialVersionUID = 3654729798147198801L;

  private Long nextStateId;

  private List<BusinessObjectInstanceDTO> businessObjects;

  public StateObjectChangeDTO() {}

  public Long getNextStateId() {
    return nextStateId;
  }

  public List<BusinessObjectInstanceDTO> getBusinessObjects() {
    return businessObjects;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("nextStateId", nextStateId).append("businessObjects", businessObjects).toString();
  }
}
