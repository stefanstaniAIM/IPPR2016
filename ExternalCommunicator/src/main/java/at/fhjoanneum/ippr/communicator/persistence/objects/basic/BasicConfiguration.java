package at.fhjoanneum.ippr.communicator.persistence.objects.basic;

import java.util.Map;

import at.fhjoanneum.ippr.communicator.persistence.objects.DataType;
import at.fhjoanneum.ippr.communicator.persistence.objects.datatypecomposer.DataTypeComposer;
import at.fhjoanneum.ippr.communicator.persistence.objects.protocol.MessageProtocol;

public interface BasicConfiguration {

  Long getId();

  String getName();

  MessageProtocol getMessageProtocol();

  Map<DataType, DataTypeComposer> getDataTypeComposer();
}
