package at.fhjoanneum.ippr.communicator.persistence.entities.basic.rest;

import java.util.Objects;

import com.google.common.base.Preconditions;

import at.fhjoanneum.ippr.communicator.persistence.entities.Builder;
import at.fhjoanneum.ippr.communicator.persistence.entities.basic.AbstractBasicConfigurationBuilder;
import at.fhjoanneum.ippr.communicator.persistence.objects.basic.RestConfiguration;

public class RestConfigurationBuilder extends AbstractBasicConfigurationBuilder
    implements Builder<RestConfiguration> {

  private String endpoint;

  RestConfigurationBuilder endpoint(final String endpoint) {
    Objects.requireNonNull(endpoint);
    this.endpoint = endpoint;
    return this;
  }

  @Override
  public RestConfiguration build() {
    Objects.requireNonNull(super.name);
    Preconditions.checkArgument(!super.composer.isEmpty());
    Objects.requireNonNull(messageProtocol);
    return new RestConfigurationImpl(name, composer, messageProtocol, endpoint);
  }
}
