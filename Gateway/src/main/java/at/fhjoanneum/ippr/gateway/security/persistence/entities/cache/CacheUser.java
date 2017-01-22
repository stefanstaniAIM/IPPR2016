package at.fhjoanneum.ippr.gateway.security.persistence.entities.cache;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class CacheUser {

  private final String systemId;
  private final String firstname;
  private final String lastname;
  private final String username;
  private final List<CacheRole> roles;
  private final String password;

  public CacheUser(final String systemId, final String firstname, final String lastname,
      final String username, final List<CacheRole> groups) {
    this(systemId, firstname, lastname, username, groups, StringUtils.EMPTY);
  }

  public CacheUser(final String systemId, final String firstname, final String lastname,
      final String username, final List<CacheRole> roles, final String password) {
    checkArgument(StringUtils.isNotBlank(systemId));
    checkArgument(StringUtils.isNotBlank(firstname));
    checkArgument(StringUtils.isNotBlank(lastname));
    checkArgument(StringUtils.isNotBlank(username));
    checkNotNull(roles);

    this.systemId = systemId;
    this.firstname = firstname;
    this.lastname = lastname;
    this.username = username;
    this.roles = roles;
    this.password = password;
  }

  public String getSystemId() {
    return systemId;
  }

  public String getFirstname() {
    return firstname;
  }

  public String getLastname() {
    return lastname;
  }


  public String getUsername() {
    return username;
  }

  public List<CacheRole> getRoles() {
    return roles;
  }

  public String getPassword() {
    return password;
  }
}
