package at.fhjoanneum.ippr.processengine.akka.messages.process.workflow;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

public class MessagesSendMessage {

  public static class Request {

    private final Long piId;
    private final List<Pair<Long, Long>> userMessageFlowIds;
    private final Long sendSubjectState;

    public Request(final Long piId, final Long sendSubjectState,
        final List<Pair<Long, Long>> userMessageFlowIds) {
      this.piId = piId;
      this.sendSubjectState = sendSubjectState;
      this.userMessageFlowIds = userMessageFlowIds;
    }

    public Long getPiId() {
      return piId;
    }

    public Long getSendSubjectState() {
      return sendSubjectState;
    }

    public List<Pair<Long, Long>> getUserMessageFlowIds() {
      return userMessageFlowIds;
    }
  }

  public static class Response {

  }
}
