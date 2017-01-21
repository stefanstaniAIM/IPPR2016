package at.fhjoanneum.ippr.gateway.security.authentication.memory;

import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import at.fhjoanneum.ippr.gateway.security.authentication.AuthenticationService;
import at.fhjoanneum.ippr.gateway.security.persistence.entities.cache.CacheUser;
import at.fhjoanneum.ippr.gateway.security.persistence.objects.User;
import at.fhjoanneum.ippr.gateway.security.rbacmapping.retrieval.RBACRetrievalService;
import at.fhjoanneum.ippr.gateway.security.repositories.RBACRepository;

public class AuthenticationServiceMemoryImpl implements AuthenticationService {

  private static final Logger LOG = LoggerFactory.getLogger(AuthenticationServiceMemoryImpl.class);

  @Autowired
  private RBACRetrievalService retrievalService;

  @Autowired
  private RBACRepository rbacRepository;

  @Override
  public Optional<User> authenticateUser(final String username, final String password) {
    if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
      return Optional.empty();
    }

    final Map<String, CacheUser> systemUsers = retrievalService.getSystemUsers();
    final CacheUser cacheUser = systemUsers.get(username);

    if (cacheUser == null) {
      LOG.info("Could not find user for username: {}", username);
      return Optional.empty();
    }
    if (!password.equals(cacheUser.getPassword())) {
      LOG.info("Wrong password for username: {}", username);
      return Optional.empty();
    }

    return rbacRepository.getUserByUsername(username);
  }

}
