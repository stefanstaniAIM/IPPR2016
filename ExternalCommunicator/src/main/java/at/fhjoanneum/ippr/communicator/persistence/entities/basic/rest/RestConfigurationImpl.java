package at.fhjoanneum.ippr.communicator.persistence.entities.basic.rest;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;

import at.fhjoanneum.ippr.communicator.persistence.entities.basic.AbstractBasicConfiguration;
import at.fhjoanneum.ippr.communicator.persistence.entities.datatypecomposer.DataTypeComposerImpl;
import at.fhjoanneum.ippr.communicator.persistence.entities.protocol.MessageProtocolImpl;
import at.fhjoanneum.ippr.communicator.persistence.objects.DataType;
import at.fhjoanneum.ippr.communicator.persistence.objects.basic.RestConfiguration;

@Entity(name = "REST_CONFIGURATION")
public class RestConfigurationImpl extends AbstractBasicConfiguration implements RestConfiguration {

  private static final long serialVersionUID = 6340416494140692436L;

  @Column
  private final String endpoint;

  RestConfigurationImpl(final String name, final Map<DataType, DataTypeComposerImpl> composer,
      final MessageProtocolImpl messageProtocol, final String endpoint) {
    super(name, composer, messageProtocol);
    this.endpoint = endpoint;
  }

  @Override
  public String getEndpoint() {
    return endpoint;
  }
}
