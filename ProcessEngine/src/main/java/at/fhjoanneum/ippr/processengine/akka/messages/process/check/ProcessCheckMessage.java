package at.fhjoanneum.ippr.processengine.akka.messages.process.check;

import java.util.List;

public class ProcessCheckMessage {

  public static class Request {
    private final Long pmId;
    private final List<Long> smIds;

    public Request(final Long pmId, final List<Long> smIds) {
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

  public static class Response {

    private final boolean isCorrect;

    public Response(final boolean isCorrect) {
      this.isCorrect = isCorrect;
    }

    public boolean isCorrect() {
      return isCorrect;
    }
  }
}
