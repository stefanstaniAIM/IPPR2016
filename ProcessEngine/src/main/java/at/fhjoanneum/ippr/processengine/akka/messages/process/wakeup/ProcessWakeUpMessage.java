package at.fhjoanneum.ippr.processengine.akka.messages.process.wakeup;

/**
 * Message to wake up process.
 */
public class ProcessWakeUpMessage {

  public static class Request {
    private final Long piId;

    public Request(final Long piId) {
      this.piId = piId;
    }

    public Long getPiId() {
      return piId;
    }
  }

  public static class Response {
    private final Long piId;

    public Response(final Long piId) {
      this.piId = piId;
    }

    public Long getPiId() {
      return piId;
    }
  }
}
