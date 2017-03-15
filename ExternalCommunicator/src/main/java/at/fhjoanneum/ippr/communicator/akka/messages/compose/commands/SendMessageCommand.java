package at.fhjoanneum.ippr.communicator.akka.messages.compose.commands;

public class SendMessageCommand {

  private final Long id;

  public SendMessageCommand(final Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }
}
