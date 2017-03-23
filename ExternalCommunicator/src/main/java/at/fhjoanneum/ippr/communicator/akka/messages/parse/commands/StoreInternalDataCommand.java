package at.fhjoanneum.ippr.communicator.akka.messages.parse.commands;

import at.fhjoanneum.ippr.communicator.persistence.objects.internal.InternalData;

public class StoreInternalDataCommand {

  private final Long id;
  private final InternalData data;

  public StoreInternalDataCommand(final Long id, final InternalData data) {
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
