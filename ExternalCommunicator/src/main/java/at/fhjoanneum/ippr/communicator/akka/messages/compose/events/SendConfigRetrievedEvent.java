package at.fhjoanneum.ippr.communicator.akka.messages.compose.events;

public class SendConfigRetrievedEvent {

  private final Long id;
  private final String sendPlugin;
  private final String body;
  private final String endpoint;
  private final String transferId;

  public SendConfigRetrievedEvent(final Long id, final String transferId, final String sendPlugin,
      final String endpoint, final String body) {
    this.id = id;
    this.transferId = transferId;
    this.sendPlugin = sendPlugin;
    this.endpoint = endpoint;
    this.body = body;
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

  public String getEndpoint() {
    return endpoint;
  }

  public String getBody() {
    return body;
  }
}
