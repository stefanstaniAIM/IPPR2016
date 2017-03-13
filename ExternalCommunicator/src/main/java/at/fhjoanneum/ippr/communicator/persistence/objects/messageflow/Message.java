package at.fhjoanneum.ippr.communicator.persistence.objects.messageflow;

public interface Message {

  Long getId();

  String getTransferId();

  void setInternalData(String data);

  String getInternalData();

  void setExternalData(String data);

  String getExternalData();
}
