package at.fhjoanneum.ippr.gateway.security.persistence.entities;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import at.fhjoanneum.ippr.gateway.security.persistence.Builder;
import at.fhjoanneum.ippr.gateway.security.persistence.objects.Group;
import at.fhjoanneum.ippr.gateway.security.persistence.objects.User;

public class UserBuilder implements Builder<User> {

  private String systemId;
  private String firstname;
  private String lastname;
  private String username;
  private final List<GroupImpl> groups = Lists.newArrayList();

  public UserBuilder systemId(final String systemId) {
    checkArgument(StringUtils.isNotBlank(systemId));
    this.systemId = systemId;
    return this;
  }

  public UserBuilder firstname(final String firstname) {
    checkArgument(StringUtils.isNotBlank(firstname));
    this.firstname = firstname;
    return this;
  }

  public UserBuilder lastname(final String lastname) {
    checkArgument(StringUtils.isNotBlank(lastname));
    this.lastname = lastname;
    return this;
  }

  public UserBuilder email(final String username) {
    checkArgument(StringUtils.isNotBlank(username));
    this.username = username;
    return this;
  }

  public UserBuilder addGroup(final Group group) {
    checkArgument(group instanceof GroupImpl);
    groups.add((GroupImpl) group);
    return this;
  }

  @Override
  public User build() {
    checkArgument(StringUtils.isNotBlank(firstname));
    checkArgument(StringUtils.isNotBlank(lastname));
    checkArgument(StringUtils.isNotBlank(username));
    checkArgument(!groups.isEmpty());

    return new UserImpl(firstname, lastname, username, groups, systemId);
  }

}
