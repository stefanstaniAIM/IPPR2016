package at.fhjoanneum.ippr.communicator.persistence.objects.datatypeparser;

import at.fhjoanneum.ippr.communicator.persistence.objects.DataType;

public interface DataTypeParser {

  Long getId();

  DataType getDataType();

  String getParserClass();

  String getDescription();
}
