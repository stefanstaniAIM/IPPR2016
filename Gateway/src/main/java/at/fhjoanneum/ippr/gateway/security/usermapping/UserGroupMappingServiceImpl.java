package at.fhjoanneum.ippr.gateway.security.usermapping;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import at.fhjoanneum.ippr.gateway.security.persistence.entities.GroupBuilder;
import at.fhjoanneum.ippr.gateway.security.persistence.entities.UserBuilder;
import at.fhjoanneum.ippr.gateway.security.persistence.entities.cache.CacheGroup;
import at.fhjoanneum.ippr.gateway.security.persistence.entities.cache.CacheUser;
import at.fhjoanneum.ippr.gateway.security.persistence.objects.Group;
import at.fhjoanneum.ippr.gateway.security.persistence.objects.User;
import at.fhjoanneum.ippr.gateway.security.persistence.repository.UserGroupRepository;
import at.fhjoanneum.ippr.gateway.security.usermapping.retrieval.UserGroupSystemRetrievalService;

@Service
public class UserGroupMappingServiceImpl implements UserGroupMappingService {

  private static final Logger LOG = LogManager.getLogger(UserGroupMappingServiceImpl.class);

  final Map<String, Group> groupCache = Maps.newHashMap();

  @Autowired
  private UserGroupSystemRetrievalService retrievalService;

  @Autowired
  private UserGroupRepository userGroupRepository;

  @Override
  public void mapUsers() {
    final Map<String, CacheUser> systemUsers = retrievalService.getSystemUsers();

    storeGroups(systemUsers);

    storeUsers(systemUsers);
  }

  @Transactional
  private void storeGroups(final Map<String, CacheUser> systemUsers) {
    final Set<CacheGroup> systemGroups = Sets.newHashSet();
    systemUsers.values().stream().map(CacheUser::getGroups)
        .forEach(group -> systemGroups.addAll(group));

    systemGroups.forEach(group -> {
      final Optional<Group> storedGroup =
          userGroupRepository.getGroupBySystemId(group.getSystemId());
      if (!storedGroup.isPresent()) {
        Group newGroup =
            new GroupBuilder().name(group.getName()).systemId(group.getSystemId()).build();
        newGroup = userGroupRepository.saveGroup(newGroup);
        groupCache.put(newGroup.getSystemId(), newGroup);
        LOG.debug("Stored new user group: {}", newGroup);
      } else if (!storedGroup.get().getName().equals(group.getName())) {
        Group updateGroup = storedGroup.get();
        updateGroup.setName(group.getName());
        updateGroup = userGroupRepository.saveGroup(updateGroup);
        groupCache.put(updateGroup.getSystemId(), updateGroup);
        LOG.debug("Updated user group: {}", storedGroup.get());
      } else {
        groupCache.put(storedGroup.get().getSystemId(), storedGroup.get());
      }
    });
  }

  @Transactional
  private void storeUsers(final Map<String, CacheUser> systemUsers) {
    systemUsers.entrySet().stream().forEach(entry -> {
      final CacheUser cacheUser = entry.getValue();
      final Optional<User> storedUser =
          userGroupRepository.getUserBySystemId(cacheUser.getSystemId());

      if (!storedUser.isPresent()) {
        final UserBuilder builder =
            new UserBuilder().systemId(cacheUser.getSystemId()).firstname(cacheUser.getFirstname())
                .lastname(cacheUser.getLastname()).username(cacheUser.getUsername());

        cacheUser.getGroups().forEach(group -> {
          final Group storedGroup = groupCache.get(group.getSystemId());
          if (storedGroup == null) {
            throw new IllegalStateException(
                "Could not find group for systemId: " + group.getSystemId());
          }
          builder.addGroup(storedGroup);
        });

        final User newUser = builder.build();
        userGroupRepository.saveUser(newUser);
        LOG.debug("Stored new user: {}", newUser);
      } else {
        final User updateUser = storedUser.get();

        if (isUserToUpdate(updateUser, cacheUser)) {
          userGroupRepository.saveUser(updateUser);
          LOG.debug("Updated user: {}", updateUser);
        }
      }
    });
  }

  private boolean isUserToUpdate(final User updateUser, final CacheUser cacheUser) {
    boolean toUpdate = false;

    if (!updateUser.getFirstname().equals(cacheUser.getFirstname())) {
      updateUser.setFirstname(cacheUser.getFirstname());
      toUpdate = true;
    }
    if (!updateUser.getLastname().equals(cacheUser.getLastname())) {
      updateUser.setLastname(cacheUser.getLastname());
      toUpdate = true;
    }
    if (updateUser.getGroups().size() != cacheUser.getGroups().size()) {
      final List<Group> newGroups = cacheUser.getGroups().stream().map(group -> {
        final Group storedGroup = groupCache.get(group.getSystemId());
        if (storedGroup == null) {
          throw new IllegalStateException(
              "Could not find group for systemId: " + group.getSystemId());
        }
        return storedGroup;
      }).collect(Collectors.toList());

      updateUser.setGroups(newGroups);
      toUpdate = true;
    }

    return toUpdate;
  }
}
