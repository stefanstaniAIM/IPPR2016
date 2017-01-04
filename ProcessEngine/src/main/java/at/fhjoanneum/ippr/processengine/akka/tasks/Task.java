package at.fhjoanneum.ippr.processengine.akka.tasks;

public interface Task<I> {

  boolean canHandle(Object obj);

  void execute(I message) throws Exception;

  <T> void registerCallback(TaskCallback<T> callback);
}
