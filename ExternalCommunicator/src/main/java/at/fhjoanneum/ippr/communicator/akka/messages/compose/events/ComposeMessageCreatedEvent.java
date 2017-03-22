package at.fhjoanneum.ippr.communicator.akka.messages.compose.events;

import at.fhjoanneum.ippr.communicator.akka.messages.AbstractEvent;

public class ComposeMessageCreatedEvent extends AbstractEvent {

  private final Long id;

  public ComposeMessageCreatedEvent(final String actorId, final Long id) {
    super(actorId);
    this.id = id;
  }

  public Long getId() {
    return id;
  }
}
