package at.fhjoanneum.ippr.communicator.persistence.entities.submission;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import at.fhjoanneum.ippr.communicator.persistence.entities.basic.inbound.BasicInboundConfigurationImpl;
import at.fhjoanneum.ippr.communicator.persistence.objects.basic.inbound.BasicInboundConfiguration;

@Entity(name = "RECEIVE_SUBMISSION")
public class ReceiveSubmission implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column
  private String transferId;

  @ManyToOne
  private BasicInboundConfigurationImpl inboundConfiguration;

  @Column
  @Enumerated(EnumType.STRING)
  private SubmissionState submissionState;

  @Column
  private final LocalDateTime createdAt;

  ReceiveSubmission() {
    this.submissionState = SubmissionState.TO_RECEIVE;
    this.createdAt = LocalDateTime.now();
  }

  ReceiveSubmission(final String transferId,
      final BasicInboundConfigurationImpl inboundConfiguration) {
    this();
    this.transferId = transferId;
    this.inboundConfiguration = inboundConfiguration;
  }

  public Long getId() {
    return id;
  }

  public String getTransferId() {
    return transferId;
  }

  public BasicInboundConfiguration getInboundConfiguration() {
    return inboundConfiguration;
  }

  public SubmissionState getSubmissionState() {
    return submissionState;
  }

  public void setToReceived() {
    this.submissionState = SubmissionState.RECEIVED;
  }

  @Override
  public String toString() {
    return "ReceiveSubmission [id=" + id + ", transferId=" + transferId + ", submissionState="
        + submissionState + "]";
  }
}
