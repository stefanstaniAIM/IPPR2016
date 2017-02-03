package at.fhjoanneum.ippr.processengine.akka.messages.analysis;

import java.time.LocalDateTime;

public class FinishedProcessesInRangeMessage {

  public static class Request {
    private final LocalDateTime start;
    private final LocalDateTime end;

    public Request(final LocalDateTime start, final LocalDateTime end) {
      this.start = start;
      this.end = end;
    }

    public LocalDateTime getStart() {
      return start;
    }

    public LocalDateTime getEnd() {
      return end;
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
