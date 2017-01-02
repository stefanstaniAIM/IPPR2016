package at.fhjoanneum.ippr.processengine.akka.tasks;

public interface Task {

  boolean canHandle(Object obj);

  void execute(Object obj) throws Exception;

  <T> void registerCallback(TaskCallback<T> callback);
}
