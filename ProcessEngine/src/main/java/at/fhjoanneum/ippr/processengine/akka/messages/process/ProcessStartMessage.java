package at.fhjoanneum.ippr.processengine.akka.messages.process;

import java.util.List;

public class ProcessStartMessage {

  private final Long pmId;
  private final List<UserGroupAssignment> userGroupAssignments;

  public ProcessStartMessage(final Long pmId,
      final List<UserGroupAssignment> userGroupAssignments) {
    this.pmId = pmId;
    this.userGroupAssignments = userGroupAssignments;
  }

  public Long getPmId() {
    return pmId;
  }

  public List<UserGroupAssignment> getUserGroupAssignments() {
    return userGroupAssignments;
  }

  @Override
  public String toString() {
    return "ProcessStartMessage [" + pmId + "]";
  }

  public static class UserGroupAssignment {
    private final Long smId;
    private final Long userId;
    private final Long groupId;

    public UserGroupAssignment(final Long smId, final Long userId, final Long groupId) {
      this.smId = smId;
      this.userId = userId;
      this.groupId = groupId;
    }

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
}
