package at.fhjoanneum.ippr.processengine.akka.messages.process;

public class ActorInitializeMessage {

  public static class Request {
    private final Long processId;

    public Request(final Long processId) {
      this.processId = processId;
    }

    public Long getProcessId() {
      return processId;
    }
  }
}
