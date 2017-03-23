package at.fhjoanneum.ippr.communicator.persistence.entities.config;

import java.util.Objects;

import com.google.common.base.Preconditions;

import at.fhjoanneum.ippr.communicator.persistence.entities.Builder;
import at.fhjoanneum.ippr.communicator.persistence.entities.basic.outbound.BasicOutboundConfigurationImpl;
import at.fhjoanneum.ippr.communicator.persistence.objects.basic.outbound.BasicOutboundConfiguration;

public class OutboundConfigurationMapBuilder implements Builder<OutboundConfigurationAssignement> {

  private Long messageFlowId;
  private BasicOutboundConfigurationImpl outboundConfiguration;

  public OutboundConfigurationMapBuilder messageFlowId(final Long messageFlowId) {
    Objects.requireNonNull(messageFlowId);
    this.messageFlowId = messageFlowId;
    return this;
  }

  public OutboundConfigurationMapBuilder outboundConfiguration(
      final BasicOutboundConfiguration config) {
    Objects.requireNonNull(config);
    Preconditions.checkArgument(config instanceof BasicOutboundConfigurationImpl);
    this.outboundConfiguration = (BasicOutboundConfigurationImpl) config;
    return this;
  }

  @Override
  public OutboundConfigurationAssignement build() {
    Objects.requireNonNull(messageFlowId);
    Objects.requireNonNull(outboundConfiguration);
    return new OutboundConfigurationAssignement(messageFlowId, outboundConfiguration);
  }

}
