package at.fhjoanneum.ippr.processengine.akka.messages.analysis;

public class ProcessesInStateMessage {

  public static class Request {
    private final String state;

    public Request(final String state) {
      this.state = state;
    }

    public String getState() {
      return state;
    }
  }

  public static class Response {
    private final Long amount;

    public Response(final Long amount) {
      this.amount = amount;
    }

    public Long getAmount() {
      return amount;
    }
  }
}
