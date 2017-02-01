package at.fhjoanneum.ippr.processengine.akka.messages.analysis;

public class ProcessesInStatePerUserMessage {

  public static class Request {
    private final Long userId;
    private final String state;

    public Request(final Long userId, final String state) {
      this.userId = userId;
      this.state = state;
    }

    public Long getUserId() {
      return userId;
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
