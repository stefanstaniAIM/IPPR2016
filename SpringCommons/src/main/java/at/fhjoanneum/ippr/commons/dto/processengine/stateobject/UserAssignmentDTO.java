package at.fhjoanneum.ippr.commons.dto.processengine.stateobject;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserAssignmentDTO implements Serializable {

  private static final long serialVersionUID = -327237293987649462L;

  private Long smId;
  private Long userId;

  public UserAssignmentDTO() {}

  public UserAssignmentDTO(final Long smId, final Long userId) {
    this.smId = smId;
    this.userId = userId;
  }

  public Long getSmId() {
    return smId;
  }

  public Long getUserId() {
    return userId;
  }


}
