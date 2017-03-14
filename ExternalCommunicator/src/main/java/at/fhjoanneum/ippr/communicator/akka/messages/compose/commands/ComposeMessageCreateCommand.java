package at.fhjoanneum.ippr.communicator.akka.messages.compose.commands;

import at.fhjoanneum.ippr.communicator.persistence.objects.internal.InternalData;

public class ComposeMessageCreateCommand {

  private final String transferId;
  private final InternalData data;

  public ComposeMessageCreateCommand(final String transferId, final InternalData data) {
    this.transferId = transferId;
    this.data = data;
  }

  public String getTransferId() {
    return transferId;
  }

  public InternalData getData() {
    return data;
  }
}
