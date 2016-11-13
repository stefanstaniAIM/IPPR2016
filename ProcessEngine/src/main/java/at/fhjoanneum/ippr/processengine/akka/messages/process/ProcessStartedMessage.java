package at.fhjoanneum.ippr.processengine.akka.messages.process;

public class ProcessStartedMessage {
  private final Long processId;

  public ProcessStartedMessage(final Long processId) {
    this.processId = processId;
  }

  public Long getProcessId() {
    return processId;
  }
}
