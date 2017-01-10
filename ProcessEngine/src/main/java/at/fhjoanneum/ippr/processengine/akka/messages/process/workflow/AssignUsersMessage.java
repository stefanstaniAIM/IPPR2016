package at.fhjoanneum.ippr.processengine.akka.messages.process.workflow;

import java.util.List;

import at.fhjoanneum.ippr.commons.dto.processengine.stateobject.UserAssignmentDTO;

public class AssignUsersMessage {
  public static class Request {

    private final Long piId;
    private final List<UserAssignmentDTO> userAssignments;

    public Request(final Long piId, final List<UserAssignmentDTO> userAssignments) {
      this.piId = piId;
      this.userAssignments = userAssignments;
    }

    public Long getPiId() {
      return piId;
    }

    public List<UserAssignmentDTO> getUserAssignments() {
      return userAssignments;
    }
  }

  public static class Response {

  }
}
