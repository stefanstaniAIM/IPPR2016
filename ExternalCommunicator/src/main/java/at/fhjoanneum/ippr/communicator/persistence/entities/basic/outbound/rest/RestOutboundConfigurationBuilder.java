package at.fhjoanneum.ippr.communicator.persistence.entities.basic.outbound.rest;

import java.util.Objects;

import com.google.common.base.Preconditions;

import at.fhjoanneum.ippr.communicator.persistence.entities.Builder;
import at.fhjoanneum.ippr.communicator.persistence.entities.basic.outbound.AbstractBasicOutboundConfigurationBuilder;
import at.fhjoanneum.ippr.communicator.persistence.objects.basic.outbound.RestOutboundConfiguration;

public class RestOutboundConfigurationBuilder extends AbstractBasicOutboundConfigurationBuilder
    implements Builder<RestOutboundConfiguration> {

  private String endpoint;

  public RestOutboundConfigurationBuilder endpoint(final String endpoint) {
    Objects.requireNonNull(endpoint);
    this.endpoint = endpoint;
    return this;
  }

  @Override
  public RestOutboundConfiguration build() {
    Objects.requireNonNull(super.name);
    Preconditions.checkArgument(!super.composer.isEmpty());
    Objects.requireNonNull(super.composerClass);
    Objects.requireNonNull(messageProtocol);
    Objects.requireNonNull(super.sendPlugin);
    return new RestOutboundConfigurationImpl(name, composer, messageProtocol, composerClass,
        sendPlugin, endpoint);
  }
}
