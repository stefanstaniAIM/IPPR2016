package at.fhjoanneum.ippr.gateway.security.persistence.entities;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;

import at.fhjoanneum.ippr.gateway.security.persistence.Builder;
import at.fhjoanneum.ippr.gateway.security.persistence.objects.Group;
import at.fhjoanneum.ippr.gateway.security.persistence.objects.User;

public class UserBuilder implements Builder<User> {

  private String systemId;
  private String firstname;
  private String lastname;
  private String email;
  private final List<GroupImpl> groups = Lists.newArrayList();

  UserBuilder systemId(final String systemId) {
    checkArgument(StringUtils.isNotBlank(systemId));
    this.systemId = systemId;
    return this;
  }

  UserBuilder firstname(final String firstname) {
    checkArgument(StringUtils.isNotBlank(firstname));
    this.firstname = firstname;
    return this;
  }

  UserBuilder lastname(final String lastname) {
    checkArgument(StringUtils.isNotBlank(lastname));
    this.lastname = lastname;
    return this;
  }

  UserBuilder email(final String email) {
    checkArgument(StringUtils.isNotBlank(email));
    this.email = email;
    return this;
  }

  UserBuilder addGroup(final Group group) {
    checkArgument(group instanceof GroupImpl);
    groups.add((GroupImpl) group);
    return this;
  }

  @Override
  public User build() {
    checkArgument(StringUtils.isNotBlank(firstname));
    checkArgument(StringUtils.isNotBlank(lastname));
    checkArgument(StringUtils.isNotBlank(email));
    checkArgument(!groups.isEmpty());

    return new UserImpl(firstname, lastname, email, groups, systemId);
  }

}
