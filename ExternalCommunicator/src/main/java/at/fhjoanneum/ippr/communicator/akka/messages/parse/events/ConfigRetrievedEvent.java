package at.fhjoanneum.ippr.communicator.akka.messages.parse.events;

import at.fhjoanneum.ippr.communicator.persistence.objects.basic.inbound.BasicInboundConfiguration;

public class ConfigRetrievedEvent {

  private final Long id;
  private final BasicInboundConfiguration basicConfiguration;
  private final String data;

  public ConfigRetrievedEvent(final Long id, final BasicInboundConfiguration basicConfiguration,
      final String data) {
    this.id = id;
    this.basicConfiguration = basicConfiguration;
    this.data = data;
  }

  public Long getId() {
    return id;
  }

  public BasicInboundConfiguration getBasicConfiguration() {
    return basicConfiguration;
  }

  public String getData() {
    return data;
  }
}
