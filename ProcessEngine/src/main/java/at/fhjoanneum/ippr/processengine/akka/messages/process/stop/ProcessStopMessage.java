package at.fhjoanneum.ippr.processengine.akka.messages.process.stop;

import at.fhjoanneum.ippr.commons.dto.processengine.ProcessInfoDTO;

public class ProcessStopMessage {

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
    private final ProcessInfoDTO process;

    public Response(final ProcessInfoDTO process) {
      this.process = process;
    }

    public ProcessInfoDTO getProcess() {
      return process;
    }
  }
}
