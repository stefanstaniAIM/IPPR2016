package at.fhjoanneum.ippr.commons.dto.communicator;

import java.io.Serializable;

public class ExternalOutputMessage implements Serializable {

  private static final long serialVersionUID = 8884909496153997468L;

  private Long transferId;



  public Long getTransferId() {
    return transferId;
  }
}
