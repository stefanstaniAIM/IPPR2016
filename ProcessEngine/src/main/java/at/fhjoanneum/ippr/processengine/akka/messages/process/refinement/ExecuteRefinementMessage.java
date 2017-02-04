package at.fhjoanneum.ippr.processengine.akka.messages.process.refinement;

public class ExecuteRefinementMessage {

  public static class Request {
    private final Long subjectStateId;

    public Request(final Long subjectStateId) {
      this.subjectStateId = subjectStateId;
    }

    public Long getSubjectStateId() {
      return subjectStateId;
    }
  }
}
