package at.fhjoanneum.ippr.processengine.akka.messages.process;

import java.util.List;

public class ProcessStartMessage {

  public static class Request {
    private final Long pmId;
    private final List<UserGroupAssignment> userGroupAssignments;

    public Request(final Long pmId, final List<UserGroupAssignment> userGroupAssignments) {
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
  }

  public static class Response {
    private final Long processId;

    public Response(final Long processId) {
      this.processId = processId;
    }

    public Long getProcessId() {
      return processId;
    }
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
