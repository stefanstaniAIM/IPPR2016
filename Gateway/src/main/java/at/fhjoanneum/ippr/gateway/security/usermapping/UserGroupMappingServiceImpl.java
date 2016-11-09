package at.fhjoanneum.ippr.gateway.security.usermapping;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;

import at.fhjoanneum.ippr.gateway.security.persistence.entities.GroupBuilder;
import at.fhjoanneum.ippr.gateway.security.persistence.entities.cache.CacheGroup;
import at.fhjoanneum.ippr.gateway.security.persistence.entities.cache.CacheUser;
import at.fhjoanneum.ippr.gateway.security.persistence.objects.Group;
import at.fhjoanneum.ippr.gateway.security.persistence.repository.UserGroupRepository;
import at.fhjoanneum.ippr.gateway.security.usermapping.retrieval.UserGroupSystemRetrievalService;

@Service
public class UserGroupMappingServiceImpl implements UserGroupMappingService {

  private static final Logger LOG = LogManager.getLogger(UserGroupMappingServiceImpl.class);

  @Autowired
  private UserGroupSystemRetrievalService retrievalService;

  @Autowired
  private UserGroupRepository userGroupRepository;

  @Override
  public void mapUsers() {
    final Map<String, CacheUser> systemUsers = retrievalService.getSystemUsers();

    storeGroups(systemUsers);
  }

  @Transactional
  private void storeGroups(final Map<String, CacheUser> systemUsers) {
    final Set<CacheGroup> systemGroups = Sets.newHashSet();
    systemUsers.values().stream().map(CacheUser::getGroups)
        .forEach(group -> systemGroups.addAll(group));

    systemGroups.forEach(group -> {
      final Optional<Group> storedGroup = userGroupRepository.getGroup(group.getSystemId());
      if (!storedGroup.isPresent()) {
        final Group newGroup =
            new GroupBuilder().name(group.getName()).systemId(group.getSystemId()).build();
        userGroupRepository.saveGroup(newGroup);
        LOG.debug("Stored new user group: {}", newGroup);
      } else if (!storedGroup.get().getName().equals(group.getName())) {
        storedGroup.get().setName(group.getName());
        userGroupRepository.saveGroup(storedGroup.get());
        LOG.debug("Updated user group: {}", storedGroup.get());
      }
    });
  }

  private void storeUsers(final Map<String, CacheUser> systemUsers) {

  }

}
