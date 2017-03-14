package at.fhjoanneum.ippr.communicator.persistence.entities.basic;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.google.common.base.Preconditions;

import at.fhjoanneum.ippr.communicator.persistence.entities.datatypecomposer.DataTypeComposerImpl;
import at.fhjoanneum.ippr.communicator.persistence.entities.protocol.MessageProtocolImpl;
import at.fhjoanneum.ippr.communicator.persistence.objects.DataType;
import at.fhjoanneum.ippr.communicator.persistence.objects.datatypecomposer.DataTypeComposer;
import at.fhjoanneum.ippr.communicator.persistence.objects.protocol.MessageProtocol;

public class AbstractBasicConfigurationBuilder {

  protected String name;
  protected final Map<DataType, DataTypeComposerImpl> parser = new HashMap<>();
  protected MessageProtocolImpl messageProtocol;
  protected String composerClass;

  public AbstractBasicConfigurationBuilder name(final String name) {
    Objects.requireNonNull(name);
    this.name = name;
    return this;
  }

  public AbstractBasicConfigurationBuilder messageProtocol(final MessageProtocol messageProtocol) {
    Objects.requireNonNull(messageProtocol);
    Preconditions.checkArgument(messageProtocol instanceof MessageProtocolImpl);
    this.messageProtocol = (MessageProtocolImpl) messageProtocol;
    return this;
  }

  public AbstractBasicConfigurationBuilder composerClass(final String composerClass) {
    Objects.requireNonNull(composerClass);
    this.composerClass = composerClass;
    return this;
  }

  public AbstractBasicConfigurationBuilder addParser(final DataTypeComposer parser) {
    Objects.requireNonNull(parser);
    Preconditions.checkArgument(parser instanceof DataTypeComposerImpl);
    this.parser.put(parser.getDataType(), (DataTypeComposerImpl) parser);
    return this;
  }
}
