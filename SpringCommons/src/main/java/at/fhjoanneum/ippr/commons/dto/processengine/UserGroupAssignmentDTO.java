package at.fhjoanneum.ippr.commons.dto.processengine;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Necessary to start processes. Contains the assignment of user/group to subject model, that is
 * used to start processes.
 *
 */
@XmlRootElement
public class UserGroupAssignmentDTO {
  private Long smId;
  private Long userId;
  private Long groupId;

  public UserGroupAssignmentDTO() {}

  public Long getSmId() {
    return smId;
  }

  public Long getUserId() {
    return userId;
  }

  public Long getGroupId() {
    return groupId;
  }
}
