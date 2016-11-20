package at.fhjoanneum.ippr.processengine.akka.messages.process;

public class ProcessStartMessage {

  private final Long pmId;

  public ProcessStartMessage(final Long pmId) {
    this.pmId = pmId;
  }

  public Long getPmId() {
    return pmId;
  }

  @Override
  public String toString() {
    return "ProcessStartMessage [" + pmId + "]";
  }
}
