package at.fhjoanneum.ippr.processengine.akka.messages.process.workflow;

import java.util.List;

public class MessagesSendMessage {

  public static class Request {

    private final Long piId;
    private final List<Long> userIds;
    private final Long sendSubjectState;

    public Request(final Long piId, final Long sendSubjectState, final List<Long> userIds) {
      this.piId = piId;
      this.sendSubjectState = sendSubjectState;
      this.userIds = userIds;
    }

    public Long getPiId() {
      return piId;
    }

    public Long getSendSubjectState() {
      return sendSubjectState;
    }

    public List<Long> getUserIds() {
      return userIds;
    }
  }

  public static class Response {

  }
}
