package at.fhjoanneum.ippr.gateway.security.persistence.repository;

import java.util.Optional;

import at.fhjoanneum.ippr.gateway.security.persistence.objects.Group;
import at.fhjoanneum.ippr.gateway.security.persistence.objects.User;

public interface UserGroupRepository {
  void saveUser(final User user);

  void saveGroup(final Group group);

  Optional<Group> getGroup(final String systemId);

}
