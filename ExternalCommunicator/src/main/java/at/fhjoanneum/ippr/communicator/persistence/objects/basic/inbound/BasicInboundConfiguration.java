package at.fhjoanneum.ippr.communicator.persistence.objects.basic.inbound;

import java.util.Map;
import java.util.Optional;

import at.fhjoanneum.ippr.communicator.persistence.objects.DataType;
import at.fhjoanneum.ippr.communicator.persistence.objects.datatypeparser.DataTypeParser;
import at.fhjoanneum.ippr.communicator.persistence.objects.protocol.MessageProtocol;

public interface BasicInboundConfiguration {

  Long getId();

  String getName();

  MessageProtocol getMessageProtocol();

  String getParserClass();

  Map<DataType, DataTypeParser> getDataTypeParser();

  Map<String, String> getConfiguration();

  Optional<String> getConfigurationEntry(String key);

}
