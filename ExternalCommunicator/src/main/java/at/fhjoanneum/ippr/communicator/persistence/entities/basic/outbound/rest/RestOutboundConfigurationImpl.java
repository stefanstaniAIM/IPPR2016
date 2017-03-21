package at.fhjoanneum.ippr.communicator.persistence.entities.basic.outbound.rest;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;

import at.fhjoanneum.ippr.communicator.persistence.entities.basic.outbound.AbstractBasicOutboundConfiguration;
import at.fhjoanneum.ippr.communicator.persistence.entities.datatypecomposer.DataTypeComposerImpl;
import at.fhjoanneum.ippr.communicator.persistence.entities.protocol.MessageProtocolImpl;
import at.fhjoanneum.ippr.communicator.persistence.objects.DataType;
import at.fhjoanneum.ippr.communicator.persistence.objects.basic.outbound.RestOutboundConfiguration;

@Entity(name = "REST_OUTBOUND_CONFIGURATION")
public class RestOutboundConfigurationImpl extends AbstractBasicOutboundConfiguration
    implements RestOutboundConfiguration {

  private static final long serialVersionUID = 6340416494140692436L;

  @Column
  private String endpoint;

  RestOutboundConfigurationImpl() {
    super();
  }

  RestOutboundConfigurationImpl(final String name, final Map<DataType, DataTypeComposerImpl> parser,
      final MessageProtocolImpl messageProtocol, final String composerClass,
      final String sendPlugin, final String endpoint) {
    super(name, parser, messageProtocol, composerClass, sendPlugin);
    this.endpoint = endpoint;
  }

  @Override
  public String getEndpoint() {
    return endpoint;
  }
}
