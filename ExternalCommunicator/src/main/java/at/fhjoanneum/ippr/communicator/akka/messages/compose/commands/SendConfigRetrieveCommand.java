package at.fhjoanneum.ippr.communicator.akka.messages.compose.commands;

public class SendConfigRetrieveCommand {

  private final Long id;
  private final Long configId;

  public SendConfigRetrieveCommand(final Long id, final Long configId) {
    this.id = id;
    this.configId = configId;
  }

  public Long getId() {
    return id;
  }

  public Long getConfigId() {
    return configId;
  }
}
