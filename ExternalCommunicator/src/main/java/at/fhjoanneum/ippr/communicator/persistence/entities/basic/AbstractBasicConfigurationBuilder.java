package at.fhjoanneum.ippr.communicator.persistence.entities.basic;

import java.util.Map;
import java.util.Objects;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import at.fhjoanneum.ippr.communicator.persistence.entities.datatypecomposer.DataTypeComposerImpl;
import at.fhjoanneum.ippr.communicator.persistence.entities.protocol.MessageProtocolImpl;
import at.fhjoanneum.ippr.communicator.persistence.objects.DataType;
import at.fhjoanneum.ippr.communicator.persistence.objects.datatypecomposer.DataTypeComposer;
import at.fhjoanneum.ippr.communicator.persistence.objects.protocol.MessageProtocol;

public class AbstractBasicConfigurationBuilder {

  protected String name;
  protected final Map<DataType, DataTypeComposerImpl> composer = Maps.newHashMap();
  protected MessageProtocolImpl messageProtocol;


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

  public AbstractBasicConfigurationBuilder addComposer(final DataTypeComposer composer) {
    Objects.requireNonNull(composer);
    Preconditions.checkArgument(composer instanceof DataTypeComposerImpl);
    this.composer.put(composer.getDataType(), (DataTypeComposerImpl) composer);
    return this;
  }
}
