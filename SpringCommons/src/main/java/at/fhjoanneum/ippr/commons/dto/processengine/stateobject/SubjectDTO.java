package at.fhjoanneum.ippr.commons.dto.processengine.stateobject;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SubjectDTO implements Serializable {

  private static final long serialVersionUID = -1944266562133136988L;

  private Long smId;
  private Long userId;
  private String assignedGroup;

  public SubjectDTO() {}

  public SubjectDTO(final Long smId, final Long userId, final String assignedGroup) {
    this.smId = smId;
    this.userId = userId;
    this.assignedGroup = assignedGroup;
  }

  public Long getSmId() {
    return smId;
  }

  public Long getUserId() {
    return userId;
  }

  public String getAssignedGroup() {
    return assignedGroup;
  }
}
