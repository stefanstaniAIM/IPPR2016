package at.fhjoanneum.ippr.processengine.akka.messages.analysis;

public class AmountOfActiveProcessesMessage {

  public static class Request {
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
