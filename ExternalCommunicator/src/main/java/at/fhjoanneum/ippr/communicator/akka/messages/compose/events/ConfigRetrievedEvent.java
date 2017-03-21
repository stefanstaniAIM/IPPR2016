package at.fhjoanneum.ippr.communicator.akka.messages.compose.events;

import at.fhjoanneum.ippr.communicator.persistence.objects.basic.outbound.BasicOutboundConfiguration;
import at.fhjoanneum.ippr.communicator.persistence.objects.internal.InternalData;

public class ConfigRetrievedEvent {

  private final Long id;
  private final String transferId;
  private final InternalData data;
  private final BasicOutboundConfiguration basicConfiguration;

  public ConfigRetrievedEvent(final Long id, final String transferId, final InternalData data,
      final BasicOutboundConfiguration basicConfiguration) {
    this.id = id;
    this.transferId = transferId;
    this.data = data;
    this.basicConfiguration = basicConfiguration;
  }

  public String getTransferId() {
    return transferId;
  }

  public InternalData getData() {
    return data;
  }

  public BasicOutboundConfiguration getBasicConfiguration() {
    return basicConfiguration;
  }

  public Long getId() {
    return id;
  }
}
