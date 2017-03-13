package at.fhjoanneum.ippr.commons.dto.communicator;

import java.io.Serializable;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ExternalOutputMessage implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long transferId;

  private Set<BusinessObject> businessObjects;

  public ExternalOutputMessage() {}

  public ExternalOutputMessage(final Long transferId, final Set<BusinessObject> businessObjects) {
    this.transferId = transferId;
    this.businessObjects = businessObjects;
  }

  public Long getTransferId() {
    return transferId;
  }

  public Set<BusinessObject> getBusinessObjects() {
    return businessObjects;
  }
}
