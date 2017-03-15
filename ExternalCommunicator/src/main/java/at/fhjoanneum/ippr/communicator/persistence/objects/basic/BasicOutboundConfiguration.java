package at.fhjoanneum.ippr.communicator.persistence.objects.basic;

import java.util.Map;

import at.fhjoanneum.ippr.communicator.persistence.objects.DataType;
import at.fhjoanneum.ippr.communicator.persistence.objects.datatypecomposer.DataTypeComposer;
import at.fhjoanneum.ippr.communicator.persistence.objects.protocol.MessageProtocol;

public interface BasicOutboundConfiguration {

  Long getId();

  String getName();

  MessageProtocol getMessageProtocol();

  String getComposerClass();

  String getSendPlugin();

  Map<DataType, DataTypeComposer> getDataTypeComposer();
}
