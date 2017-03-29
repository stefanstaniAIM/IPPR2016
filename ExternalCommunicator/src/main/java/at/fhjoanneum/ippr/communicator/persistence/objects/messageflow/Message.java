package at.fhjoanneum.ippr.communicator.persistence.objects.messageflow;

import at.fhjoanneum.ippr.communicator.persistence.objects.basic.inbound.BasicInboundConfiguration;
import at.fhjoanneum.ippr.communicator.persistence.objects.basic.outbound.BasicOutboundConfiguration;

public interface Message {

  Long getId();

  String getTransferId();

  void setTransferId(String transferId);

  void setInternalData(String data);

  String getInternalData();

  void setExternalData(String data);

  String getExternalData();

  MessageState getMessageState();

  void setMessageState(MessageState messageState);

  void setOutboundConfiguration(BasicOutboundConfiguration outboundConfiguration);

  BasicOutboundConfiguration getOutboundConfiguration();

  void setInboundConfiguration(BasicInboundConfiguration inboundConfiguration);

  BasicInboundConfiguration getInboundConfiguration();
}
