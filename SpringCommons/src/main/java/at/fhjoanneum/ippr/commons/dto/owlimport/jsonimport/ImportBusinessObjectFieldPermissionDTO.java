package at.fhjoanneum.ippr.commons.dto.owlimport.jsonimport;

import java.io.Serializable;

public class ImportBusinessObjectFieldPermissionDTO implements Serializable {

  private static final long serialVersionUID = -5822425865281817037L;

  private String bofmId;
  private String stateId;
  private boolean read;
  private boolean write;
  private boolean mandatory;

  public ImportBusinessObjectFieldPermissionDTO() {}

  public ImportBusinessObjectFieldPermissionDTO(final String bofmId, final String stateId,
      final boolean read, final boolean write, final boolean mandatory) {
    this.bofmId = bofmId;
    this.stateId = stateId;
    this.read = read;
    this.write = write;
    this.mandatory = mandatory;
  }



  public String getBofmId() {
    return bofmId;
  }

  public String getStateId() {
    return stateId;
  }

  public boolean isRead() {
    return read;
  }

  public boolean isWrite() {
    return write;
  }

  public boolean isMandatory() {
    return mandatory;
  }
}
