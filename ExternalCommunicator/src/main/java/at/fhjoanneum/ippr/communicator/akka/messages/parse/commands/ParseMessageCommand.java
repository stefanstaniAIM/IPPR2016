package at.fhjoanneum.ippr.communicator.akka.messages.parse.commands;

public class ParseMessageCommand {

  private final Long id;

  public ParseMessageCommand(final Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }
}
