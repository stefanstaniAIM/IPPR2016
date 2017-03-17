package at.fhjoanneum.ippr.communicator.persistence.entities.config;

import java.util.Objects;

import com.google.common.base.Preconditions;

import at.fhjoanneum.ippr.communicator.persistence.entities.Builder;
import at.fhjoanneum.ippr.communicator.persistence.entities.basic.AbstractBasicOutboundConfiguration;
import at.fhjoanneum.ippr.communicator.persistence.objects.basic.BasicOutboundConfiguration;

public class OutboundConfigurationMapBuilder implements Builder<OutboundConfigurationMap> {

  private Long messageFlowId;
  private AbstractBasicOutboundConfiguration outboundConfiguration;

  public OutboundConfigurationMapBuilder messageFlowId(final Long messageFlowId) {
    Objects.requireNonNull(messageFlowId);
    this.messageFlowId = messageFlowId;
    return this;
  }

  public OutboundConfigurationMapBuilder outboundConfiguration(
      final BasicOutboundConfiguration config) {
    Objects.requireNonNull(config);
    Preconditions.checkArgument(config instanceof AbstractBasicOutboundConfiguration);
    this.outboundConfiguration = (AbstractBasicOutboundConfiguration) config;
    return this;
  }

  @Override
  public OutboundConfigurationMap build() {
    Objects.requireNonNull(messageFlowId);
    Objects.requireNonNull(outboundConfiguration);
    return new OutboundConfigurationMap(messageFlowId, outboundConfiguration);
  }

}
