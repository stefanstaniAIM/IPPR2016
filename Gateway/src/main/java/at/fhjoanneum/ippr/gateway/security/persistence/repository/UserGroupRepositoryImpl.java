package at.fhjoanneum.ippr.gateway.security.persistence.repository;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import at.fhjoanneum.ippr.gateway.security.persistence.entities.GroupImpl;
import at.fhjoanneum.ippr.gateway.security.persistence.entities.UserImpl;
import at.fhjoanneum.ippr.gateway.security.persistence.objects.Group;
import at.fhjoanneum.ippr.gateway.security.persistence.objects.User;

@Repository
public class UserGroupRepositoryImpl implements UserGroupRepository {

  private static final Logger LOG = LogManager.getLogger(UserGroupRepositoryImpl.class);

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private GroupRepository groupRepository;

  @Override
  public void saveUser(final User user) {
    userRepository.save((UserImpl) user);
  }

  @Override
  public Optional<Group> getGroup(final String systemId) {
    return Optional.ofNullable(groupRepository.findBySystemId(systemId));
  }

  @Override
  public void saveGroup(final Group group) {
    groupRepository.save((GroupImpl) group);
  }

  interface UserRepository extends PagingAndSortingRepository<UserImpl, Long> {
  }

  interface GroupRepository extends PagingAndSortingRepository<GroupImpl, Long> {

    @Query(value = "SELECT * FROM USER_GROUP WHERE SYSTEM_ID = :systemId", nativeQuery = true)
    GroupImpl findBySystemId(@Param("systemId") String systemId);
  }
}
