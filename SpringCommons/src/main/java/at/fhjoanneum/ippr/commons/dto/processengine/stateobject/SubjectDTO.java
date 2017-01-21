package at.fhjoanneum.ippr.commons.dto.processengine.stateobject;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SubjectDTO implements Serializable {

  private static final long serialVersionUID = -1944266562133136988L;

  private Long smId;
  private Long userId;
  private String assignedGroup;
  private List<String> assignedRules;

  public SubjectDTO() {}

  public SubjectDTO(final Long smId, final Long userId, final String assignedGroup,
      final List<String> assignedRules) {
    this.smId = smId;
    this.userId = userId;
    this.assignedGroup = assignedGroup;
    this.assignedRules = assignedRules;
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

  public List<String> getAssignedRules() {
    return assignedRules;
  }
}
