package at.fhjoanneum.ippr.commons.dto.pmstorage;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FieldPermissionDTO implements Serializable {

  static final long serialVersionUID = -1084893374166053627L;

  private String name;

  public FieldPermissionDTO() {}

  public FieldPermissionDTO(final String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
