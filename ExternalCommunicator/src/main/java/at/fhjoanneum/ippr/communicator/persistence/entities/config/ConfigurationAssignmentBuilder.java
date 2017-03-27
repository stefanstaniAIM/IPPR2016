package at.fhjoanneum.ippr.communicator.persistence.entities.config;

import java.util.Objects;

import com.google.common.base.Preconditions;

import at.fhjoanneum.ippr.communicator.persistence.entities.Builder;
import at.fhjoanneum.ippr.communicator.persistence.entities.basic.inbound.BasicInboundConfigurationImpl;
import at.fhjoanneum.ippr.communicator.persistence.entities.basic.outbound.BasicOutboundConfigurationImpl;
import at.fhjoanneum.ippr.communicator.persistence.objects.basic.inbound.BasicInboundConfiguration;
import at.fhjoanneum.ippr.communicator.persistence.objects.basic.outbound.BasicOutboundConfiguration;

public class ConfigurationAssignmentBuilder implements Builder<ConfigurationAssignement> {

  private Long messageFlowId;
  private BasicOutboundConfigurationImpl outboundConfiguration;
  private BasicInboundConfigurationImpl inboundConfiguration;



  public ConfigurationAssignmentBuilder messageFlowId(final Long messageFlowId) {
    Objects.requireNonNull(messageFlowId);
    this.messageFlowId = messageFlowId;
    return this;
  }

  public ConfigurationAssignmentBuilder outboundConfiguration(
      final BasicOutboundConfiguration config) {
    Objects.requireNonNull(config);
    Preconditions.checkArgument(config instanceof BasicOutboundConfigurationImpl);
    this.outboundConfiguration = (BasicOutboundConfigurationImpl) config;
    return this;
  }

  public ConfigurationAssignmentBuilder inboundConfiguration(
      final BasicInboundConfiguration config) {
    Objects.requireNonNull(config);
    Preconditions.checkArgument(config instanceof BasicInboundConfigurationImpl);
    this.inboundConfiguration = (BasicInboundConfigurationImpl) config;
    return this;
  }

  @Override
  public ConfigurationAssignement build() {
    Objects.requireNonNull(messageFlowId);
    Preconditions.checkArgument(outboundConfiguration != null || inboundConfiguration != null);
    if (outboundConfiguration != null) {
      return new ConfigurationAssignement(messageFlowId, outboundConfiguration);
    }
    return new ConfigurationAssignement(messageFlowId, inboundConfiguration);
  }

}
