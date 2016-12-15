package at.fhjoanneum.ippr.commons.dto.processengine;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BusinessObjectFieldInstanceDTO implements Serializable {

  private static final long serialVersionUID = -2396191843338173896L;

  private Long bofmId;
  private Long bofiId;
  private String permission;

  public BusinessObjectFieldInstanceDTO() {}

  public BusinessObjectFieldInstanceDTO(final Long bofmId, final Long bofiId,
      final String permission) {
    this.bofmId = bofmId;
    this.bofiId = bofiId;
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
}
