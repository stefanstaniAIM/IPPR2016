package at.fhjoanneum.ippr.communicator.persistence.entities.basic.outbound;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.google.common.base.Preconditions;

import at.fhjoanneum.ippr.communicator.persistence.entities.Builder;
import at.fhjoanneum.ippr.communicator.persistence.entities.datatypecomposer.DataTypeComposerImpl;
import at.fhjoanneum.ippr.communicator.persistence.entities.protocol.MessageProtocolImpl;
import at.fhjoanneum.ippr.communicator.persistence.objects.DataType;
import at.fhjoanneum.ippr.communicator.persistence.objects.basic.outbound.BasicOutboundConfiguration;
import at.fhjoanneum.ippr.communicator.persistence.objects.datatypecomposer.DataTypeComposer;
import at.fhjoanneum.ippr.communicator.persistence.objects.protocol.MessageProtocol;

public class BasicOutboundConfigurationBuilder implements Builder<BasicOutboundConfiguration> {

  private String name;
  private final Map<DataType, DataTypeComposerImpl> composer = new HashMap<>();
  private final Map<String, String> configuration = new HashMap<>();
  private MessageProtocolImpl messageProtocol;
  private String composerClass;
  private String sendPlugin;

  public BasicOutboundConfigurationBuilder name(final String name) {
    Objects.requireNonNull(name);
    this.name = name;
    return this;
  }

  public BasicOutboundConfigurationBuilder messageProtocol(final MessageProtocol messageProtocol) {
    Objects.requireNonNull(messageProtocol);
    Preconditions.checkArgument(messageProtocol instanceof MessageProtocolImpl);
    this.messageProtocol = (MessageProtocolImpl) messageProtocol;
    return this;
  }

  public BasicOutboundConfigurationBuilder composerClass(final String composerClass) {
    Objects.requireNonNull(composerClass);
    this.composerClass = composerClass;
    return this;
  }

  public BasicOutboundConfigurationBuilder sendPlugin(final String sendPlugin) {
    Objects.requireNonNull(sendPlugin);
    this.sendPlugin = sendPlugin;
    return this;
  }

  public BasicOutboundConfigurationBuilder addComposer(final DataTypeComposer composer) {
    Objects.requireNonNull(composer);
    Preconditions.checkArgument(composer instanceof DataTypeComposerImpl);
    this.composer.put(composer.getDataType(), (DataTypeComposerImpl) composer);
    return this;
  }

  public BasicOutboundConfigurationBuilder addConfigurationEntry(final String key,
      final String value) {
    Objects.requireNonNull(key);
    Objects.requireNonNull(value);
    this.configuration.put(key, value);
    return this;
  }

  @Override
  public BasicOutboundConfiguration build() {
    Objects.requireNonNull(name);
    Objects.requireNonNull(messageProtocol);
    Objects.requireNonNull(composerClass);
    Preconditions.checkArgument(!composer.isEmpty());
    Objects.requireNonNull(sendPlugin);

    return new BasicOutboundConfigurationImpl(name, messageProtocol, composerClass, sendPlugin,
        composer, configuration);
  }
}
