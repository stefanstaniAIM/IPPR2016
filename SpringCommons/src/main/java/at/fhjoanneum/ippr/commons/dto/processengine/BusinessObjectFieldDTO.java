package at.fhjoanneum.ippr.commons.dto.processengine;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BusinessObjectFieldDTO implements Serializable {

  private static final long serialVersionUID = -2396191843338173896L;

  private Long bofmId;
  private Long bofiId;
  private String name;
  private String type;
  private boolean mandatory;
  private String permission;


  public BusinessObjectFieldDTO() {}

  public BusinessObjectFieldDTO(final Long bofmId, final Long bofiId, final String name,
      final String type, final boolean mandatory, final String permission) {
    this.bofmId = bofmId;
    this.bofiId = bofiId;
    this.name = name;
    this.type = type;
    this.mandatory = mandatory;
    this.permission = permission;
  }


  public Long getBofmId() {
    return bofmId;
  }

  public Long getBofiId() {
    return bofiId;
  }

  public String getPermission() {
    return permission;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public boolean isMandatory() {
    return mandatory;
  }
}
