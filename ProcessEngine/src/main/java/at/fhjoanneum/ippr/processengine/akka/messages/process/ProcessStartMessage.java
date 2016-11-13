package at.fhjoanneum.ippr.processengine.akka.messages.process;

public class ProcessStartMessage {

  private final Long processId;

  public ProcessStartMessage(final Long processId) {
    this.processId = processId;
  }

  public Long getProcessId() {
    return processId;
  }

  @Override
  public String toString() {
    return "ProcessStartMessage [" + processId + "]";
  }
}
