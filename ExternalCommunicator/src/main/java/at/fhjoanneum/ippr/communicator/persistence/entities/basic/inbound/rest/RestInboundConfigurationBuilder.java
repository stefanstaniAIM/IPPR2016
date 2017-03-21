package at.fhjoanneum.ippr.communicator.persistence.entities.basic.inbound.rest;

import java.util.Objects;

import com.google.common.base.Preconditions;

import at.fhjoanneum.ippr.communicator.persistence.entities.Builder;
import at.fhjoanneum.ippr.communicator.persistence.entities.basic.inbound.AbstractBasicInboundConfigurationBuilder;
import at.fhjoanneum.ippr.communicator.persistence.objects.basic.inbound.RestInboundConfiguration;

public class RestInboundConfigurationBuilder extends AbstractBasicInboundConfigurationBuilder
    implements Builder<RestInboundConfiguration> {

  private String endpoint;

  public RestInboundConfigurationBuilder endpoint(final String endpoint) {
    Objects.requireNonNull(endpoint);
    this.endpoint = endpoint;
    return this;
  }

  @Override
  public RestInboundConfiguration build() {
    Objects.requireNonNull(super.name);
    Preconditions.checkArgument(!super.parser.isEmpty());
    Objects.requireNonNull(super.parser);
    Objects.requireNonNull(messageProtocol);
    return new RestInboundConfigurationImpl(name, messageProtocol, parserClass, endpoint);
  }
}
