package at.fhjoanneum.ippr.processengine.akka.messages.process.workflow;

import at.fhjoanneum.ippr.commons.dto.processengine.SubjectStateDTO;

public class StateObjectMessage {

  public static class Request {
    private final Long piId;
    private final Long userId;

    public Request(final Long piId, final Long userId) {
      this.piId = piId;
      this.userId = userId;
    }

    public Long getPiId() {
      return piId;
    }

    public Long getUserId() {
      return userId;
    }
  }

  public static class Response {
    private final SubjectStateDTO subjectStateDTO;

    public Response(final SubjectStateDTO subjectStateDTO) {
      this.subjectStateDTO = subjectStateDTO;
    }

    public SubjectStateDTO getSubjectStateDTO() {
      return subjectStateDTO;
    }
  }
}
