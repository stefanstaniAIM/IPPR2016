package at.fhjoanneum.ippr.commons.dto.user;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RoleDTO {

  private String roleName;
  private Long roleId;

  public RoleDTO() {}

  public RoleDTO(final Long roleId, final String roleName) {
    this.roleName = roleName;
    this.roleId = roleId;
  }

  public Long getRoleId() {
    return roleId;
  }

  public String getRoleName() {
    return roleName;
  }
}
