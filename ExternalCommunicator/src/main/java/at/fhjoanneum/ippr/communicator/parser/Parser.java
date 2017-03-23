package at.fhjoanneum.ippr.communicator.parser;

import java.util.Map;

import at.fhjoanneum.ippr.communicator.persistence.objects.DataType;
import at.fhjoanneum.ippr.communicator.persistence.objects.datatypeparser.DataTypeParser;
import at.fhjoanneum.ippr.communicator.persistence.objects.internal.InternalData;
import at.fhjoanneum.ippr.communicator.persistence.objects.protocol.MessageProtocol;

public interface Parser {

  InternalData parse(String input, MessageProtocol messageProtocol,
      Map<DataType, DataTypeParser> parser, Map<String, String> configuration) throws Exception;

  String getDescription();
}
