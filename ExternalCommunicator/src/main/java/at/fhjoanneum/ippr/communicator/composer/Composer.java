package at.fhjoanneum.ippr.communicator.composer;

import java.util.Map;

import at.fhjoanneum.ippr.communicator.persistence.objects.DataType;
import at.fhjoanneum.ippr.communicator.persistence.objects.datatypecomposer.DataTypeComposer;
import at.fhjoanneum.ippr.communicator.persistence.objects.internal.InternalData;
import at.fhjoanneum.ippr.communicator.persistence.objects.protocol.MessageProtocol;

public interface Composer {

  String compose(String transferId, InternalData data, MessageProtocol messageProtocol,
      Map<DataType, DataTypeComposer> composer, final Map<String, String> configuration);

  String getDescription();
}
