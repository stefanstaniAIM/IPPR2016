package at.fhjoanneum.ippr.processengine.akka.tasks;

public enum TaskAllocation {

  STATE_OBJECT_CHANGE_TASK("StateObjectChangeTask");

  private final String actorName;

  private TaskAllocation(final String actorName) {
    this.actorName = actorName;
  }

  String getActorName() {
    return actorName;
  }
}
