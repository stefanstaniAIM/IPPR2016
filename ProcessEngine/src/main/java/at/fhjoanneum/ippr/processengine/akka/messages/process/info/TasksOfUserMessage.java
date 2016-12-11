package at.fhjoanneum.ippr.processengine.akka.messages.process.info;

import java.util.List;

import at.fhjoanneum.ippr.commons.dto.processengine.TaskDTO;

public class TasksOfUserMessage {

  public static class Request {

    private final Long userId;

    public Request(final Long userId) {
      this.userId = userId;
    }

    public Long getUserId() {
      return userId;
    }
  }

  public static class Response {

    private final List<TaskDTO> tasks;

    public Response(final List<TaskDTO> tasks) {
      this.tasks = tasks;
    }

    public List<TaskDTO> getTasks() {
      return tasks;
    }
  }
}
