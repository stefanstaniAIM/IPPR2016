package at.fhjoanneum.ippr.communicator.akka.messages.compose.events;

public class ComposedMessageEvent extends AbstracComposeEvent {
  private final Long id;

  public ComposedMessageEvent(final String actorId, final Long id) {
    super(actorId);
    this.id = id;
  }

  public Long getId() {
    return id;
  }
}
