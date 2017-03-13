package at.fhjoanneum.ippr.communicator.persistence.entities.messageflow;

import com.google.common.base.Preconditions;

import at.fhjoanneum.ippr.communicator.persistence.entities.Builder;
import at.fhjoanneum.ippr.communicator.persistence.objects.messageflow.Message;

public class MessageBuilder implements Builder<Message> {

  private String transferid;

  public MessageBuilder transferId(final String transferId) {
    Preconditions.checkNotNull(transferId);
    this.transferid = transferId;
    return this;
  }

  @Override
  public Message build() {
    Preconditions.checkNotNull(transferid);
    return new MessageImpl(transferid);
  }

}
