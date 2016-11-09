package at.fhjoanneum.ippr.gateway.security.persistence.repository;

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

}
