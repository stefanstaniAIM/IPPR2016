package at.fhjoanneum.ippr.processengine.akka.messages.process.info;

import java.util.List;

import at.fhjoanneum.ippr.commons.dto.processengine.ProcessInfoDTO;

public class ProcessInfoMessage {

  public static class Request {
    private Long user;
    private final String state;
    private final int page;
    private final int size;

    public Request(final String state, final int page, final int size) {
      this.state = state;
      this.page = page;
      this.size = size;
    }

    public Request(final Long user, final String state, final int page, final int size) {
      this(state, page, size);
      this.user = user;
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

    public Long getUser() {
      return user;
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
