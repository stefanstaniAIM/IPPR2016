package at.fhjoanneum.ippr.processengine.akka.messages.process.timeout;

public class TimeoutExecuteMessage {

  private final Long ssId;

  public TimeoutExecuteMessage(final Long ssId) {
    this.ssId = ssId;
  }

  public Long getSsId() {
    return ssId;
  }
}
