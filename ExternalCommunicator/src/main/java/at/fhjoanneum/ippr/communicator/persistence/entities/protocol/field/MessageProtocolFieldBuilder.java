package at.fhjoanneum.ippr.communicator.persistence.entities.protocol.field;

import java.util.Objects;

import com.google.common.base.Preconditions;

import at.fhjoanneum.ippr.communicator.persistence.entities.Builder;
import at.fhjoanneum.ippr.communicator.persistence.entities.protocol.MessageProtocolImpl;
import at.fhjoanneum.ippr.communicator.persistence.objects.DataType;
import at.fhjoanneum.ippr.communicator.persistence.objects.protocol.MessageProtocol;
import at.fhjoanneum.ippr.communicator.persistence.objects.protocol.MessageProtocolField;

public class MessageProtocolFieldBuilder implements Builder<MessageProtocolField> {

  private MessageProtocolImpl messageProtocol;
  private String externalName;
  private String internalName;
  private DataType dataType;
  private boolean mandatory;
  private String defaultValue;

  public MessageProtocolFieldBuilder messageProtocol(final MessageProtocol messageProtocol) {
    Objects.requireNonNull(messageProtocol);
    Preconditions.checkArgument(messageProtocol instanceof MessageProtocolImpl);
    this.messageProtocol = (MessageProtocolImpl) messageProtocol;
    return this;
  }

  public MessageProtocolFieldBuilder externalName(final String externalName) {
    Objects.requireNonNull(externalName);
    this.externalName = externalName;
    return this;
  }

  public MessageProtocolFieldBuilder internalName(final String internalName) {
    Objects.requireNonNull(internalName);
    this.internalName = internalName;
    return this;
  }

  public MessageProtocolFieldBuilder defaultValue(final String defaultValue) {
    Objects.requireNonNull(defaultValue);
    this.defaultValue = defaultValue;
    return this;
  }

  public MessageProtocolFieldBuilder dataType(final DataType dataType) {
    Objects.requireNonNull(dataType);
    this.dataType = dataType;
    return this;
  }

  public MessageProtocolFieldBuilder mandatory(final boolean mandatory) {
    this.mandatory = mandatory;
    return this;
  }

  @Override
  public MessageProtocolField build() {
    Objects.requireNonNull(messageProtocol);
    Objects.requireNonNull(dataType);
    Objects.requireNonNull(externalName);
    Objects.requireNonNull(internalName);
    return new MessageProtocolFieldImpl(messageProtocol, externalName, internalName, dataType,
        mandatory, defaultValue);
  }

}
