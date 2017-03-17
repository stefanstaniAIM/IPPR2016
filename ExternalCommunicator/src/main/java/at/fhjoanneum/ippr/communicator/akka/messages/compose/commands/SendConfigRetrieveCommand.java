package at.fhjoanneum.ippr.communicator.akka.messages.compose.commands;

public class SendConfigRetrieveCommand {

  private final Long id;

  public SendConfigRetrieveCommand(final Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }
}
