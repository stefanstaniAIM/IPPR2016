package at.fhjoanneum.ippr.communicator.akka.messages.parse.events;

import at.fhjoanneum.ippr.communicator.persistence.objects.internal.InternalData;

public class NotifyConfigRetrievedEvent {

  private final Long id;
  private final String transferId;
  private final InternalData data;

  public NotifyConfigRetrievedEvent(final Long id, final String transferId,
      final InternalData data) {
    this.id = id;
    this.transferId = transferId;
    this.data = data;
  }

  public Long getId() {
    return id;
  }

  public String getTransferId() {
    return transferId;
  }

  public InternalData getData() {
    return data;
  }
}
