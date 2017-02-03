package at.fhjoanneum.ippr.processengine.akka.messages.analysis;

import java.time.LocalDateTime;

public class FinishedProcessesInRangeForUserMessage {

  public static class Request {
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final Long user;

    public Request(final LocalDateTime start, final LocalDateTime end, final Long user) {
      this.start = start;
      this.end = end;
      this.user = user;
    }

    public LocalDateTime getStart() {
      return start;
    }

    public LocalDateTime getEnd() {
      return end;
    }

    public Long getUser() {
      return user;
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
