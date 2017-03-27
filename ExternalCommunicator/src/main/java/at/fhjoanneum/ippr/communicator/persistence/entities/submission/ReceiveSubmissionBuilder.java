package at.fhjoanneum.ippr.communicator.persistence.entities.submission;

import java.util.Objects;

import com.google.common.base.Preconditions;

import at.fhjoanneum.ippr.communicator.persistence.entities.Builder;
import at.fhjoanneum.ippr.communicator.persistence.entities.basic.inbound.BasicInboundConfigurationImpl;
import at.fhjoanneum.ippr.communicator.persistence.objects.basic.inbound.BasicInboundConfiguration;

public class ReceiveSubmissionBuilder implements Builder<ReceiveSubmission> {

  private String transferId;
  private BasicInboundConfigurationImpl basicInboundConfiguration;

  public ReceiveSubmissionBuilder transferId(final String transferId) {
    Objects.requireNonNull(transferId);
    this.transferId = transferId;
    return this;
  }

  public ReceiveSubmissionBuilder basicInboundConfiguration(
      final BasicInboundConfiguration basicInboundConfiguration) {
    Objects.requireNonNull(basicInboundConfiguration);
    Preconditions.checkArgument(basicInboundConfiguration instanceof BasicInboundConfigurationImpl);
    this.basicInboundConfiguration = (BasicInboundConfigurationImpl) basicInboundConfiguration;
    return this;
  }

  @Override
  public ReceiveSubmission build() {
    Objects.requireNonNull(transferId);
    Objects.requireNonNull(basicInboundConfiguration);
    return new ReceiveSubmission(transferId, basicInboundConfiguration);
  }

}
