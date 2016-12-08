package at.fhjoanneum.ippr.processengine.akka.messages.process;

import java.util.List;

import at.fhjoanneum.ippr.commons.dto.processengine.ProcessInfoDTO;

public class ProcessInfoMessage {

  public static class Request {
    private final String state;
    private final int page;
    private final int size;

    public Request(final String state, final int page, final int size) {
      this.state = state;
      this.page = page;
      this.size = size;
    }

    public String getState() {
      return state;
    }

    public int getPage() {
      return page;
    }

    public int getSize() {
      return size;
    }
  }

  public static class Response {
    private final List<ProcessInfoDTO> processes;

    public Response(final List<ProcessInfoDTO> processes) {
      this.processes = processes;
    }

    public List<ProcessInfoDTO> getProcesses() {
      return processes;
    }
  }
}
