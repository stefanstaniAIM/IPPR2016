package at.fhjoanneum.ippr.commons.dto.processengine.user;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserDTO implements Serializable {

  private static final long serialVersionUID = 851993404912331997L;

  private String username;
  private String firstname;
  private String lastname;

  public UserDTO() {}

  public UserDTO(final String username, final String firstname, final String lastname) {
    this.username = username;
    this.firstname = firstname;
    this.lastname = lastname;
  }

  public String getUsername() {
    return username;
  }

  public String getFirstname() {
    return firstname;
  }

  public String getLastname() {
    return lastname;
  }
}
