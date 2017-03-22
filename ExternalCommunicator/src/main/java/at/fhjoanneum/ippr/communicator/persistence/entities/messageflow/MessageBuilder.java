package at.fhjoanneum.ippr.communicator.persistence.entities.messageflow;

import com.google.common.base.Preconditions;

import at.fhjoanneum.ippr.communicator.persistence.entities.Builder;
import at.fhjoanneum.ippr.communicator.persistence.objects.messageflow.Message;
import at.fhjoanneum.ippr.communicator.persistence.objects.messageflow.MessageState;

public class MessageBuilder implements Builder<Message> {

  private String transferid;
  private MessageState messageState;

  public MessageBuilder transferId(final String transferId) {
    Preconditions.checkNotNull(transferId);
    this.transferid = transferId;
    return this;
  }

  public MessageBuilder messageState(final MessageState messageState) {
    Preconditions.checkNotNull(messageState);
    this.messageState = messageState;
    return this;
  }

  @Override
  public Message build() {
    Preconditions.checkNotNull(messageState);
    return new MessageImpl(transferid, messageState);
  }

}
