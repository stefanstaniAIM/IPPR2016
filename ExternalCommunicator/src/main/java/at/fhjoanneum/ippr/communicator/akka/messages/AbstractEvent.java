package at.fhjoanneum.ippr.communicator.akka.messages;

public abstract class AbstractEvent {

  private final String actorId;

  protected AbstractEvent(final String actorId) {
    this.actorId = actorId;
  }

  public String getActorId() {
    return actorId;
  }
}
