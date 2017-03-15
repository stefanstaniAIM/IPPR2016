package at.fhjoanneum.ippr.communicator.plugins.send;

public interface SendPlugin {

  boolean send(String body, String endpoint);
}
