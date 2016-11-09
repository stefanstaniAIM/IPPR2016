package at.fhjoanneum.ippr.gateway.security.persistence.repository;

import java.util.Optional;

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

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private GroupRepository groupRepository;

  @Override
  public User saveUser(final User user) {
    return userRepository.save((UserImpl) user);
  }

  @Override
  public Group saveGroup(final Group group) {
    return groupRepository.save((GroupImpl) group);
  }

  @Override
  public Optional<User> getUserByUserId(final Long userId) {
    return Optional.ofNullable(userRepository.findOne(userId));
  }

  @Override
  public Optional<User> getUserBySystemId(final String systemId) {
    return Optional.ofNullable(userRepository.findBySystemId(systemId));
  }

  @Override
  public Optional<User> getUserByUsername(final String username) {
    return Optional.ofNullable(userRepository.findByUsername(username));
  }


  @Override
  public Optional<Group> getGroupBySystemId(final String systemId) {
    return Optional.ofNullable(groupRepository.findBySystemId(systemId));
  }

  interface UserRepository extends PagingAndSortingRepository<UserImpl, Long> {

    @Query(value = "SELECT * FROM USER WHERE SYSTEM_ID = :systemId", nativeQuery = true)
    UserImpl findBySystemId(@Param("systemId") String systemId);

    @Query(value = "SELECT * FROM USER WHERE USERNAME = :username", nativeQuery = true)
    UserImpl findByUsername(@Param("username") String username);
  }

  interface GroupRepository extends PagingAndSortingRepository<GroupImpl, Long> {

    @Query(value = "SELECT * FROM USER_GROUP WHERE SYSTEM_ID = :systemId", nativeQuery = true)
    GroupImpl findBySystemId(@Param("systemId") String systemId);
  }
}
