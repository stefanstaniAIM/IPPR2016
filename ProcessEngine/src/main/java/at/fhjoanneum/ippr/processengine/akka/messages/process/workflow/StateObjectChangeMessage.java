package at.fhjoanneum.ippr.processengine.akka.messages.process.workflow;

import at.fhjoanneum.ippr.commons.dto.processengine.stateobject.StateObjectChangeDTO;

public class StateObjectChangeMessage {

  public static class Request {
    private final Long piId;
    private final Long userId;
    private final StateObjectChangeDTO stateObjectChangeDTO;

    public Request(final Long piId, final Long userId,
        final StateObjectChangeDTO stateObjectChangeDTO) {
      this.piId = piId;
      this.userId = userId;
      this.stateObjectChangeDTO = stateObjectChangeDTO;
    }

    public Long getPiId() {
      return piId;
    }

    public Long getUserId() {
      return userId;
    }

    public StateObjectChangeDTO getStateObjectChangeDTO() {
      return stateObjectChangeDTO;
    }
  }

  public static class Response {
    private final Long piId;
    private final Boolean finished;

    public Response(final Long piId, final Boolean finished) {
      this.piId = piId;
      this.finished = finished;
    }

    public Long getPiId() {
      return piId;
    }

    public Boolean isFinished() {
      return finished;
    }
  }
}
