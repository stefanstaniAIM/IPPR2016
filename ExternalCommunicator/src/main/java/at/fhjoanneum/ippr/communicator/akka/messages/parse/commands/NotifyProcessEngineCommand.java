package at.fhjoanneum.ippr.communicator.akka.messages.parse.commands;

public class NotifyProcessEngineCommand {

  private final Long id;

  public NotifyProcessEngineCommand(final Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }
}
