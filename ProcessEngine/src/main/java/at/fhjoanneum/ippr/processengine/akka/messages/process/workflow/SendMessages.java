package at.fhjoanneum.ippr.processengine.akka.messages.process.workflow;

import java.util.List;

public class SendMessages {

  public static class Request {

    private final Long piId;
    private final List<Long> userIds;

    public Request(final Long piId, final List<Long> userIds) {
      this.piId = piId;
      this.userIds = userIds;
    }

    public Long getPiId() {
      return piId;
    }

    public List<Long> getUserIds() {
      return userIds;
    }
  }

  public static class Response {

  }
}
