package at.fhjoanneum.ippr.communicator.composer;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xembly.Directives;
import org.xembly.ImpossibleModificationException;
import org.xembly.Xembler;

import at.fhjoanneum.ippr.communicator.composer.datatype.ComposerUtils;
import at.fhjoanneum.ippr.communicator.global.GlobalKey;
import at.fhjoanneum.ippr.communicator.persistence.objects.DataType;
import at.fhjoanneum.ippr.communicator.persistence.objects.datatypecomposer.DataTypeComposer;
import at.fhjoanneum.ippr.communicator.persistence.objects.internal.InternalData;
import at.fhjoanneum.ippr.communicator.persistence.objects.internal.InternalObject;
import at.fhjoanneum.ippr.communicator.persistence.objects.protocol.MessageProtocol;
import at.fhjoanneum.ippr.communicator.persistence.objects.protocol.MessageProtocolField;

public class XmlComposer implements Composer {

  private static final Logger LOG = LoggerFactory.getLogger(XmlComposer.class);

  private String transferIdKey = "TRANSFER_ID";


  @Override
  public String compose(final String transferId, final InternalData data,
      final MessageProtocol messageProtocol, final Map<DataType, DataTypeComposer> composer,
      final Map<String, String> configuration) {
    transferIdKey = configuration.get(GlobalKey.TRANSFER_ID);

    final Directives root = new Directives().add(messageProtocol.getExternalName());
    root.attr(transferIdKey, transferId);

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
      root.attr(protocolField.getExternalName(), value);
    }

    try {
      return new Xembler(root).xml();
    } catch (final ImpossibleModificationException e) {
      LOG.error(e.getMessage());
      return null;
    }
  }

  @Override
  public String getDescription() {
    // TODO Auto-generated method stub
    return null;
  }

}
