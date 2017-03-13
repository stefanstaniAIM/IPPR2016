package at.fhjoanneum.ippr.communicator.akka.messages.compose.events;

public class ComposeMessageCreatedEvent extends AbstracComposeEvent {

  private final Long id;

  public ComposeMessageCreatedEvent(final String actorId, final Long id) {
    super(actorId);
    this.id = id;
  }

  public Long getId() {
    return id;
  }
}
