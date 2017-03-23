package at.fhjoanneum.ippr.communicator.akka.messages.compose.events;

import java.util.HashMap;
import java.util.Map;

public class SendConfigRetrievedEvent {

  private final Long id;
  private final String sendPlugin;
  private final String body;
  private final String transferId;
  private Map<String, String> configuration = new HashMap<>();

  public SendConfigRetrievedEvent(final Long id, final String transferId, final String sendPlugin,
      final String body, final Map<String, String> configuration) {
    this.id = id;
    this.transferId = transferId;
    this.sendPlugin = sendPlugin;
    this.body = body;
    this.configuration = configuration;
  }

  public Long getId() {
    return id;
  }

  public String getTransferId() {
    return transferId;
  }

  public String getSendPlugin() {
    return sendPlugin;
  }

  public String getBody() {
    return body;
  }

  public Map<String, String> getConfiguration() {
    return configuration;
  }
}
