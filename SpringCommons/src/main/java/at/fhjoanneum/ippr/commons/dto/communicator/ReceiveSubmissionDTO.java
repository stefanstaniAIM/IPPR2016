package at.fhjoanneum.ippr.commons.dto.communicator;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ReceiveSubmissionDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private String transferId;

  public ReceiveSubmissionDTO() {}

  public ReceiveSubmissionDTO(final String transferId) {
    this.transferId = transferId;
  }

  public String getTransferId() {
    return transferId;
  }
}
