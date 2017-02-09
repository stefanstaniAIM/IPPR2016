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
