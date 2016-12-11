package at.fhjoanneum.ippr.processengine.akka.messages.process.info;

import at.fhjoanneum.ippr.commons.dto.processengine.ProcessStateDTO;

/**
 * Message to retrieve state information of process instance.
 *
 */
public class ProcessStateMessage {

  public static class Request {
    private final Long piId;

    public Request(final Long piId) {
      this.piId = piId;
    }

    public Long getPiId() {
      return piId;
    }
  }

  public static class Response {
    private final ProcessStateDTO processStateDTO;

    public Response(final ProcessStateDTO processStateDTO) {
      this.processStateDTO = processStateDTO;
    }

    public ProcessStateDTO getProcessStateDTO() {
      return processStateDTO;
    }
  }
}
