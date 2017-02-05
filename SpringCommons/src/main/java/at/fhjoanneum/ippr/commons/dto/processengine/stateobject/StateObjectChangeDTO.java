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

  private List<UserAssignmentDTO> userAssignments;

  public StateObjectChangeDTO() {}

  public StateObjectChangeDTO(final Long nextStateId,
      final List<BusinessObjectInstanceDTO> businessObjects,
      final List<UserAssignmentDTO> userAssignments) {
    this.nextStateId = nextStateId;
    this.businessObjects = businessObjects;
    this.userAssignments = userAssignments;
  }

  public Long getNextStateId() {
    return nextStateId;
  }

  public List<BusinessObjectInstanceDTO> getBusinessObjects() {
    return businessObjects;
  }

  public List<UserAssignmentDTO> getUserAssignments() {
    return userAssignments;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("nextStateId", nextStateId).append("businessObjects", businessObjects)
        .append("userAssignments", userAssignments).toString();
  }
}
