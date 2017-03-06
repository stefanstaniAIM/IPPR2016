package at.fhjoanneum.ippr.communicator.persistence.objects.protocol;

import at.fhjoanneum.ippr.communicator.persistence.objects.DataType;

public interface MessageProtocolField {

  Long getId();

  MessageProtocol getMessageProtocol();

  String getExternalName();

  String getInternalName();

  DataType getDataType();

  boolean isMandatory();

  String getDefaultValue();
}
