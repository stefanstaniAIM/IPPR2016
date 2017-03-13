package at.fhjoanneum.ippr.communicator.akka.messages.compose.commands;

public class ComposeMessageCommand {
  private final Long id;

  public ComposeMessageCommand(final Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }
}
