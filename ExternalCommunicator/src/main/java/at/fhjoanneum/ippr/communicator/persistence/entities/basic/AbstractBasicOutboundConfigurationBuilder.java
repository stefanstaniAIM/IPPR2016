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

public class AbstractBasicOutboundConfigurationBuilder {

  protected String name;
  protected final Map<DataType, DataTypeComposerImpl> composer = new HashMap<>();
  protected MessageProtocolImpl messageProtocol;
  protected String composerClass;
  protected String sendPlugin;

  public AbstractBasicOutboundConfigurationBuilder name(final String name) {
    Objects.requireNonNull(name);
    this.name = name;
    return this;
  }

  public AbstractBasicOutboundConfigurationBuilder messageProtocol(
      final MessageProtocol messageProtocol) {
    Objects.requireNonNull(messageProtocol);
    Preconditions.checkArgument(messageProtocol instanceof MessageProtocolImpl);
    this.messageProtocol = (MessageProtocolImpl) messageProtocol;
    return this;
  }

  public AbstractBasicOutboundConfigurationBuilder composerClass(final String composerClass) {
    Objects.requireNonNull(composerClass);
    this.composerClass = composerClass;
    return this;
  }

  public AbstractBasicOutboundConfigurationBuilder sendPlugin(final String sendPlugin) {
    Objects.requireNonNull(sendPlugin);
    this.sendPlugin = sendPlugin;
    return this;
  }

  public AbstractBasicOutboundConfigurationBuilder addComposer(final DataTypeComposer composer) {
    Objects.requireNonNull(composer);
    Preconditions.checkArgument(composer instanceof DataTypeComposerImpl);
    this.composer.put(composer.getDataType(), (DataTypeComposerImpl) composer);
    return this;
  }
}
