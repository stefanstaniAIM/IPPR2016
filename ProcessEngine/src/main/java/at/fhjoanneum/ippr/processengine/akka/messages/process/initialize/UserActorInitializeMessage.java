package at.fhjoanneum.ippr.processengine.akka.messages.process.initialize;

public class UserActorInitializeMessage {

  public static class Request {
    private final Long piId;
    private final Long sId;

    public Request(final Long piId, final Long sId) {
      this.piId = piId;
      this.sId = sId;
    }

    public Long getPiId() {
      return piId;
    }

    public Long getSId() {
      return sId;
    }
  }
}
