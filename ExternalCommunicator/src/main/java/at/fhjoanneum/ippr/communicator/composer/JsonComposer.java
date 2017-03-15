package at.fhjoanneum.ippr.communicator.composer;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

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

  private static final Logger LOG = LoggerFactory.getLogger(JsonComposer.class);

  @Override
  public String compose(final String transferId, final InternalData data,
      final MessageProtocol messageProtocol, final Map<DataType, DataTypeComposer> composer) {
    try {
      final JSONObject json = new JSONObject();
      json.put(TRANSFER_ID, transferId);
      json.put(TYPE, messageProtocol.getExternalName());

      final String currentMessage = messageProtocol.getInternalName();
      final InternalObject internalObject = data.getObjects().get(currentMessage);
      for (final MessageProtocolField protocolField : messageProtocol.getFields()) {
        if (protocolField.isMandatory() && ((internalObject == null)
            || (internalObject.getFields().get(protocolField.getInternalName()) == null))) {
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
        json.put(protocolField.getExternalName(), value);
      }

      return json.toString();
    } catch (final Exception e) {
      LOG.error(e.getMessage());
      return null;
    }
  }

  @Override
  public String getDescription() {
    return "Json Composer";
  }

}
