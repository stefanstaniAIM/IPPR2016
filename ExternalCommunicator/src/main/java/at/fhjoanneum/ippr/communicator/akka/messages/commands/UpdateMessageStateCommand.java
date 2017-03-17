package at.fhjoanneum.ippr.communicator.akka.messages.commands;

import at.fhjoanneum.ippr.communicator.persistence.objects.messageflow.MessageState;

public class UpdateMessageStateCommand {

  private final Long id;
  private final MessageState messageState;

  public UpdateMessageStateCommand(final Long id, final MessageState messageState) {
    this.id = id;
    this.messageState = messageState;
  }

  public Long getId() {
    return id;
  }

  public MessageState getMessageState() {
    return messageState;
  }
}
