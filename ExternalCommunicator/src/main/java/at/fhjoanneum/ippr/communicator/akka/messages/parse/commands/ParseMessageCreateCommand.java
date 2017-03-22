package at.fhjoanneum.ippr.communicator.akka.messages.parse.commands;

public class ParseMessageCreateCommand {

  private final Long configId;
  private final String data;

  public ParseMessageCreateCommand(final String data, final Long configId) {
    this.data = data;
    this.configId = configId;
  }

  public String getData() {
    return data;
  }

  public Long getConfigId() {
    return configId;
  }
}
