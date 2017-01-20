package at.fhjoanneum.ippr.processengine.akka.messages.process.workflow;

public class MessageReceiveMessage {

  public static class Request {
    private final Long piId;
    private final Long userId;
    private final Long mfId;

    public Request(final Long piId, final Long userId, final Long mfId) {
      this.piId = piId;
      this.userId = userId;
      this.mfId = mfId;
    }

    public Long getPiId() {
      return piId;
    }

    public Long getUserId() {
      return userId;
    }

    public Long getMfId() {
      return mfId;
    }
  }

  public static class Response {

  }
}
