package at.fhjoanneum.ippr.communicator.akka.messages.commands;

public class ConfigRetrievalCommand {

  private final Long id;

  public ConfigRetrievalCommand(final Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }
}
