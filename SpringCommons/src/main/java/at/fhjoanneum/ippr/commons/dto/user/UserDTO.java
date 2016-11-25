package at.fhjoanneum.ippr.commons.dto.user;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserDTO implements Serializable {

  private static final long serialVersionUID = 7692113492836757571L;

  private Long uId;
  private String firstname;
  private String lastname;
  private String groupname;

  public UserDTO() {}

  public UserDTO(final Long uId, final String firstname, final String lastname,
      final String groupname) {
    this.uId = uId;
    this.firstname = firstname;
    this.lastname = lastname;
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

  public String getGroupname() {
    return groupname;
  }
}
