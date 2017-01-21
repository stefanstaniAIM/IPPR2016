package at.fhjoanneum.ippr.commons.dto.user;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserDTO implements Serializable {

  private static final long serialVersionUID = 7692113492836757571L;

  private Long uId;
  private String firstname;
  private String lastname;
  private List<RoleDTO> roles;
  private List<RuleDTO> rules;

  public UserDTO() {}

  public UserDTO(final Long uId, final String firstname, final String lastname,
      final List<RoleDTO> roles, final List<RuleDTO> rules) {
    this.uId = uId;
    this.firstname = firstname;
    this.lastname = lastname;
    this.roles = roles;
    this.rules = rules;
  }

  public Long getUId() {
    return uId;
  }

  public String getFirstname() {
    return firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public List<RoleDTO> getRoles() {
    return roles;
  }

  public List<RuleDTO> getRules() {
    return rules;
  }
}
