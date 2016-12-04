package at.fhjoanneum.ippr.processengine.akka.messages.process.check;

public class ProcessCheckMessage {

  public static class Request {
    private final Long pmId;

    public Request(final Long pmId) {
      this.pmId = pmId;
    }

    public Long getPmId() {
      return pmId;
    }
  }

  public static class Response {

    private final boolean isCorrect;

    public Response(final boolean isCorrect) {
      this.isCorrect = isCorrect;
    }

    public boolean isCorrect() {
      return isCorrect;
    }
  }
}
