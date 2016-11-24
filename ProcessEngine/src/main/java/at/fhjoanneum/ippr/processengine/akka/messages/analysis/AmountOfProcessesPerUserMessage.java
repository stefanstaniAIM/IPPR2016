package at.fhjoanneum.ippr.processengine.akka.messages.analysis;

import at.fhjoanneum.ippr.persistence.objects.engine.enums.ProcessInstanceState;

public class AmountOfProcessesPerUserMessage {

  public static class Request {
    private final Long userId;
    private final ProcessInstanceState state;

    public Request(final Long userId, final ProcessInstanceState state) {
      this.userId = userId;
      this.state = state;
    }

    public Long getUserId() {
      return userId;
    }

    public ProcessInstanceState getState() {
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
