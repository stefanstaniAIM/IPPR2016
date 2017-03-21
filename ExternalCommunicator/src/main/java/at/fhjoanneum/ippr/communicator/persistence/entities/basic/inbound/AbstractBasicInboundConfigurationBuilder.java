package at.fhjoanneum.ippr.communicator.persistence.entities.basic.inbound;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.google.common.base.Preconditions;

import at.fhjoanneum.ippr.communicator.persistence.entities.datatypeparser.DataTypeParserImpl;
import at.fhjoanneum.ippr.communicator.persistence.entities.protocol.MessageProtocolImpl;
import at.fhjoanneum.ippr.communicator.persistence.objects.DataType;
import at.fhjoanneum.ippr.communicator.persistence.objects.datatypeparser.DataTypeParser;
import at.fhjoanneum.ippr.communicator.persistence.objects.protocol.MessageProtocol;

public class AbstractBasicInboundConfigurationBuilder {

  protected String name;
  protected final Map<DataType, DataTypeParserImpl> parser = new HashMap<>();
  protected MessageProtocolImpl messageProtocol;
  protected String parserClass;

  public AbstractBasicInboundConfigurationBuilder name(final String name) {
    Objects.requireNonNull(name);
    this.name = name;
    return this;
  }

  public AbstractBasicInboundConfigurationBuilder messageProtocol(
      final MessageProtocol messageProtocol) {
    Objects.requireNonNull(messageProtocol);
    Preconditions.checkArgument(messageProtocol instanceof MessageProtocolImpl);
    this.messageProtocol = (MessageProtocolImpl) messageProtocol;
    return this;
  }

  public AbstractBasicInboundConfigurationBuilder parserClass(final String parserClass) {
    Objects.requireNonNull(parserClass);
    this.parserClass = parserClass;
    return this;
  }

  public AbstractBasicInboundConfigurationBuilder addParser(final DataTypeParser parser) {
    Objects.requireNonNull(parser);
    Preconditions.checkArgument(parser instanceof DataTypeParserImpl);
    this.parser.put(parser.getDataType(), (DataTypeParserImpl) parser);
    return this;
  }
}
