package at.fhjoanneum.ippr.communicator.akka.messages.compose.events;

public abstract class AbstracComposeEvent {

  private final String actorId;

  protected AbstracComposeEvent(final String actorId) {
    this.actorId = actorId;
  }

  public String getActorId() {
    return actorId;
  }
}
