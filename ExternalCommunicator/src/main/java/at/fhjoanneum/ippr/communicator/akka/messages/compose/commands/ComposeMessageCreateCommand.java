package at.fhjoanneum.ippr.communicator.akka.messages.compose.commands;

import at.fhjoanneum.ippr.communicator.persistence.objects.internal.InternalData;

public class ComposeMessageCreateCommand {

  private final String transferId;
  private final Long configId;
  private final InternalData data;

  public ComposeMessageCreateCommand(final String transferId, final InternalData data,
      final Long configId) {
    this.transferId = transferId;
    this.data = data;
    this.configId = configId;
  }

  public String getTransferId() {
    return transferId;
  }

  public InternalData getData() {
    return data;
  }

  public Long getConfigId() {
    return configId;
  }
}
