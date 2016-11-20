package at.fhjoanneum.ippr.processengine.akka.messages.process.check;

import java.util.List;

public class ProcessToCheckMessage {
  private final Long pmId;
  private final List<Long> smIds;

  public ProcessToCheckMessage(final Long pmId, final List<Long> smIds) {
    this.pmId = pmId;
    this.smIds = smIds;
  }

  public Long getPmId() {
    return pmId;
  }

  public List<Long> getSmIds() {
    return smIds;
  }
}
