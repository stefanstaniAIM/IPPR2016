package at.fhjoanneum.ippr.communicator.composer;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonObject;

import at.fhjoanneum.ippr.communicator.composer.datatype.ComposerUtils;
import at.fhjoanneum.ippr.communicator.persistence.objects.DataType;
import at.fhjoanneum.ippr.communicator.persistence.objects.datatypecomposer.DataTypeComposer;
import at.fhjoanneum.ippr.communicator.persistence.objects.internal.InternalData;
import at.fhjoanneum.ippr.communicator.persistence.objects.internal.InternalObject;
import at.fhjoanneum.ippr.communicator.persistence.objects.protocol.MessageProtocol;
import at.fhjoanneum.ippr.communicator.persistence.objects.protocol.MessageProtocolField;

@Transactional(isolation = Isolation.READ_COMMITTED)
public class JsonComposer implements Composer {

  private static final String TYPE = "TYPE";
  private static final String TRANSFER_ID = "TRANSFER_ID";

  @Override
  public String compose(final String transferId, final InternalData data,
      final MessageProtocol messageProtocol, final Map<DataType, DataTypeComposer> composer) {
    final JsonObject json = new JsonObject();
    json.addProperty(TRANSFER_ID, transferId);
    json.addProperty(TYPE, messageProtocol.getExternalName());

    final String currentMessage = messageProtocol.getInternalName();
    final InternalObject internalObject = data.getObjects().get(currentMessage);
    for (final MessageProtocolField protocolField : messageProtocol.getFields()) {
      if (protocolField.isMandatory() && (internalObject == null
          || internalObject.getFields().get(protocolField.getInternalName()) == null)) {
        throw new IllegalArgumentException("Could not find protocol field for internal name ["
            + protocolField.getInternalName() + "]");
      }

      String value = StringUtils.EMPTY;
      if (internalObject != null) {
        value = internalObject.getFields().get(protocolField.getInternalName()) != null
            ? internalObject.getFields().get(protocolField.getInternalName()).getValue()
            : protocolField.getDefaultValue();
      }

      value = ComposerUtils.compose(value, protocolField.getDataType());
      json.addProperty(protocolField.getExternalName(), value);
    }

    return json.toString();
  }

  @Override
  public String getDescription() {
    return "Json Composer";
  }

}
