package at.fhjoanneum.ippr.processengine.akka.tasks;

@FunctionalInterface
public interface TaskCallback<T> {

  void callback(T value);
}
