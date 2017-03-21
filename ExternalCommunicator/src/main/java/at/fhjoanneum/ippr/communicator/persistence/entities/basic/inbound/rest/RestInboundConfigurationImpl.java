package at.fhjoanneum.ippr.communicator.persistence.entities.basic.inbound.rest;

import javax.persistence.Column;
import javax.persistence.Entity;

import at.fhjoanneum.ippr.communicator.persistence.entities.basic.inbound.AbstractBasicInboundConfiguration;
import at.fhjoanneum.ippr.communicator.persistence.entities.protocol.MessageProtocolImpl;
import at.fhjoanneum.ippr.communicator.persistence.objects.basic.inbound.RestInboundConfiguration;

@Entity(name = "REST_INBOUND_CONFIGURATION")
public class RestInboundConfigurationImpl extends AbstractBasicInboundConfiguration
    implements RestInboundConfiguration {

  private static final long serialVersionUID = 1L;

  @Column
  private String endpoint;

  RestInboundConfigurationImpl() {
    super();
  }

  RestInboundConfigurationImpl(final String name, final MessageProtocolImpl messageProtocol,
      final String parserClass, final String endpoint) {
    super(name, messageProtocol, parserClass);
    this.endpoint = endpoint;
  }

  @Override
  public String getEndpoint() {
    return endpoint;
  }
}
