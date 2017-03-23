package at.fhjoanneum.ippr.communicator.akka.messages.parse.events;

import at.fhjoanneum.ippr.communicator.persistence.objects.internal.InternalData;

public class NotifyConfigRetrievedEvent {

  private final Long id;
  private final InternalData data;

  public NotifyConfigRetrievedEvent(final Long id, final InternalData data) {
    this.id = id;
    this.data = data;
  }

  public Long getId() {
    return id;
  }

  public InternalData getData() {
    return data;
  }
}
