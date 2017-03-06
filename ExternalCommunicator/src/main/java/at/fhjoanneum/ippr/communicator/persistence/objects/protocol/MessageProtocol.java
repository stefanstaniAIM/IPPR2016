package at.fhjoanneum.ippr.communicator.persistence.objects.protocol;

import java.util.List;

public interface MessageProtocol {

  Long getId();

  String getExternalName();

  String getInternalName();

  List<MessageProtocolField> getFields();

  List<MessageProtocol> getChildren();

  MessageProtocol getParent();
}
