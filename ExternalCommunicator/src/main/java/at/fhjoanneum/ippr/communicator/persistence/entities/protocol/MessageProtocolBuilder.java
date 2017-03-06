package at.fhjoanneum.ippr.communicator.persistence.entities.protocol;

import java.util.Objects;

import com.google.common.base.Preconditions;

import at.fhjoanneum.ippr.communicator.persistence.entities.Builder;
import at.fhjoanneum.ippr.communicator.persistence.objects.protocol.MessageProtocol;

public class MessageProtocolBuilder implements Builder<MessageProtocol> {

  private String externalName;
  private String internalName;
  private MessageProtocolImpl parent;

  public MessageProtocolBuilder parent(final MessageProtocol parent) {
    Objects.requireNonNull(parent);
    Preconditions.checkArgument(parent instanceof MessageProtocolImpl);
    this.parent = (MessageProtocolImpl) parent;
    return this;
  }

  public MessageProtocolBuilder externalName(final String externalName) {
    Objects.requireNonNull(externalName);
    this.externalName = externalName;
    return this;
  }

  public MessageProtocolBuilder internalName(final String internalName) {
    Objects.requireNonNull(internalName);
    this.internalName = internalName;
    return this;
  }

  @Override
  public MessageProtocol build() {
    Objects.requireNonNull(externalName);
    Objects.requireNonNull(internalName);
    if (parent == null) {
      return new MessageProtocolImpl(externalName, internalName);
    } else {
      return new MessageProtocolImpl(parent, externalName, internalName);
    }
  }

}
