package at.fhjoanneum.ippr.communicator.persistence.entities.basic.inbound;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.google.common.base.Preconditions;

import at.fhjoanneum.ippr.communicator.persistence.entities.Builder;
import at.fhjoanneum.ippr.communicator.persistence.entities.datatypeparser.DataTypeParserImpl;
import at.fhjoanneum.ippr.communicator.persistence.entities.protocol.MessageProtocolImpl;
import at.fhjoanneum.ippr.communicator.persistence.objects.DataType;
import at.fhjoanneum.ippr.communicator.persistence.objects.basic.inbound.BasicInboundConfiguration;
import at.fhjoanneum.ippr.communicator.persistence.objects.datatypeparser.DataTypeParser;
import at.fhjoanneum.ippr.communicator.persistence.objects.protocol.MessageProtocol;

public class BasicInboundConfigurationBuilder implements Builder<BasicInboundConfiguration> {

  private String name;
  private final Map<DataType, DataTypeParserImpl> parser = new HashMap<>();
  private final Map<String, String> configuration = new HashMap<>();
  private MessageProtocolImpl messageProtocol;
  private String parserClass;

  public BasicInboundConfigurationBuilder name(final String name) {
    Objects.requireNonNull(name);
    this.name = name;
    return this;
  }

  public BasicInboundConfigurationBuilder messageProtocol(final MessageProtocol messageProtocol) {
    Objects.requireNonNull(messageProtocol);
    Preconditions.checkArgument(messageProtocol instanceof MessageProtocolImpl);
    this.messageProtocol = (MessageProtocolImpl) messageProtocol;
    return this;
  }

  public BasicInboundConfigurationBuilder parserClass(final String parserClass) {
    Objects.requireNonNull(parserClass);
    this.parserClass = parserClass;
    return this;
  }

  public BasicInboundConfigurationBuilder addParser(final DataTypeParser parser) {
    Objects.requireNonNull(parser);
    Preconditions.checkArgument(parser instanceof DataTypeParserImpl);
    this.parser.put(parser.getDataType(), (DataTypeParserImpl) parser);
    return this;
  }

  public BasicInboundConfigurationBuilder addConfigurationEntry(final String key,
      final String value) {
    Objects.requireNonNull(key);
    Objects.requireNonNull(value);
    this.configuration.put(key, value);
    return this;
  }

  @Override
  public BasicInboundConfiguration build() {
    Objects.requireNonNull(name);
    Objects.requireNonNull(messageProtocol);
    Objects.requireNonNull(parserClass);
    Preconditions.checkArgument(!parser.isEmpty());

    return new BasicInboundConfigurationImpl(name, messageProtocol, parserClass, parser,
        configuration);
  }
}
