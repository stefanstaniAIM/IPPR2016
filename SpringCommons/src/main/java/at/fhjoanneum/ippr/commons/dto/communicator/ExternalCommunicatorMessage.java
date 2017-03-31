package at.fhjoanneum.ippr.commons.dto.communicator;

import java.io.Serializable;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ExternalCommunicatorMessage implements Serializable {

  private static final long serialVersionUID = 1L;

  private String transferId;

  private Set<BusinessObject> businessObjects;

  public ExternalCommunicatorMessage() {}

  public ExternalCommunicatorMessage(final String transferId, final Set<BusinessObject> businessObjects) {
    this.transferId = transferId;
    this.businessObjects = businessObjects;
  }

  public String getTransferId() {
    return transferId;
  }

  public Set<BusinessObject> getBusinessObjects() {
    return businessObjects;
  }

  @Override
  public String toString() {
    return "ExternalOutputMessage [transferId=" + transferId + ", businessObjects="
        + businessObjects + "]";
  }
}
