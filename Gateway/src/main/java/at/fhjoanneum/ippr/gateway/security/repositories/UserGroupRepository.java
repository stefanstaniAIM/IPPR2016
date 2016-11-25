package at.fhjoanneum.ippr.gateway.security.repositories;

import java.util.List;
import java.util.Optional;

import at.fhjoanneum.ippr.gateway.security.persistence.objects.Group;
import at.fhjoanneum.ippr.gateway.security.persistence.objects.User;

public interface UserGroupRepository {

  User saveUser(final User user);

  Group saveGroup(final Group group);

  Optional<User> getUserByUserId(final Long userId);

  Optional<User> getUserBySystemId(final String systemId);

  Optional<User> getUserByUsername(final String username);

  Optional<Group> getGroupBySystemId(final String systemId);

  Optional<Group> getGroupByGroupName(final String groupName);

  List<User> getUsersByGroupName(final String groupName);
}
