package at.fhjoanneum.ippr.communicator.plugins.send;

import java.util.Map;

public interface SendPlugin {

  boolean send(String body, Map<String, String> configuration);
}
