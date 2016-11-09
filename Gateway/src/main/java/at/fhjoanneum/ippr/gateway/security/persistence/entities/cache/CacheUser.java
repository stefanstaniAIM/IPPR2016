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
  private final List<CacheGroup> groups;
  private final String password;

  public CacheUser(final String systemId, final String firstname, final String lastname,
      final String username, final List<CacheGroup> groups) {
    this(systemId, firstname, lastname, username, groups, StringUtils.EMPTY);
  }

  public CacheUser(final String systemId, final String firstname, final String lastname,
      final String username, final List<CacheGroup> groups, final String password) {
    checkArgument(StringUtils.isNotBlank(systemId));
    checkArgument(StringUtils.isNotBlank(firstname));
    checkArgument(StringUtils.isNotBlank(lastname));
    checkArgument(StringUtils.isNotBlank(username));
    checkNotNull(groups);

    this.systemId = systemId;
    this.firstname = firstname;
    this.lastname = lastname;
    this.username = username;
    this.groups = groups;
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

  public List<CacheGroup> getGroups() {
    return groups;
  }

  public String getPassword() {
    return password;
  }
}
