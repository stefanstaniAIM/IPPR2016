package at.fhjoanneum.ippr.communicator.akka.messages.compose.commands;

public class ComposeMessageCreateCommand {

  private final String transferId;

  public ComposeMessageCreateCommand(final String transferId) {
    this.transferId = transferId;
  }

  public String getTransferId() {
    return transferId;
  }
}
