package at.fhjoanneum.ippr.processengine.akka.messages.process.workflow;

import at.fhjoanneum.ippr.commons.dto.processengine.stateobject.StateObjectDTO;

public class StateObjectMessage {

  public static class Request {
    private final Long piId;
    private final Long userId;
    private final boolean internal;

    public Request(final Long piId, final Long userId, final boolean internal) {
      this.piId = piId;
      this.userId = userId;
      this.internal = internal;
    }

    public Long getPiId() {
      return piId;
    }

    public Long getUserId() {
      return userId;
    }

    public boolean isInternal() {
      return internal;
    }
  }

  public static class Response {
    private final StateObjectDTO stateObjectDTO;

    public Response(final StateObjectDTO stateObjectDTO) {
      this.stateObjectDTO = stateObjectDTO;
    }

    public StateObjectDTO getStateObjectDTO() {
      return stateObjectDTO;
    }
  }
}
