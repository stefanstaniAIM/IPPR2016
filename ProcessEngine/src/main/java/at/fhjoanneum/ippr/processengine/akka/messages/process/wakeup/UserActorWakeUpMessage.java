package at.fhjoanneum.ippr.processengine.akka.messages.process.wakeup;

/**
 * Message to wake up user.
 */
public class UserActorWakeUpMessage {

  public static class Request {
    private final Long userId;

    public Request(final Long userId) {
      this.userId = userId;
    }

    public Long getUserId() {
      return userId;
    }
  }

  public static class Response {
    private final Long userId;

    public Response(final Long userId) {
      this.userId = userId;
    }

    public Long getUserId() {
      return userId;
    }
  }

}
