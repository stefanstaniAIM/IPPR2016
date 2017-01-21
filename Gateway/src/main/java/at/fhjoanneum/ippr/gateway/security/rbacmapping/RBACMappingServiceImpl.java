package at.fhjoanneum.ippr.gateway.security.rbacmapping;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;

import at.fhjoanneum.ippr.gateway.security.persistence.entities.RoleBuilder;
import at.fhjoanneum.ippr.gateway.security.persistence.entities.RuleBuilder;
import at.fhjoanneum.ippr.gateway.security.persistence.entities.UserBuilder;
import at.fhjoanneum.ippr.gateway.security.persistence.entities.cache.CacheRole;
import at.fhjoanneum.ippr.gateway.security.persistence.entities.cache.CacheUser;
import at.fhjoanneum.ippr.gateway.security.persistence.objects.Role;
import at.fhjoanneum.ippr.gateway.security.persistence.objects.Rule;
import at.fhjoanneum.ippr.gateway.security.persistence.objects.User;
import at.fhjoanneum.ippr.gateway.security.rbacmapping.retrieval.RBACRetrievalService;
import at.fhjoanneum.ippr.gateway.security.repositories.RBACRepository;

@Service
@Transactional
public class RBACMappingServiceImpl implements RBACMappingService {

  private static final Logger LOG = LoggerFactory.getLogger(RBACMappingServiceImpl.class);

  @Autowired
  private RBACRetrievalService retrievalService;

  @Autowired
  private RBACRepository rbacRepository;

  private final Map<String, Rule> rulesCache = Maps.newHashMap();
  private final Map<String, Role> rolesCache = Maps.newHashMap();

  @Override
  @Async
  public void mapUsers() {
    LOG.info("Start user mapping");
    final Map<String, CacheUser> users = retrievalService.getSystemUsers();
    storeRules(users);
    storeRoles(users);
    storeUsers(users);
    LOG.info("Finished user mapping");

  }

  private void storeRules(final Map<String, CacheUser> users) {
    users.values().stream().map(CacheUser::getRoles).flatMap(List::stream).map(CacheRole::getRules)
        .flatMap(List::stream).forEach(rule -> {
          final Optional<Rule> ruleOpt = rbacRepository.getRuleBySystemId(rule.getSystemId());
          if (!ruleOpt.isPresent()) {
            final Rule dbRule =
                new RuleBuilder().systemId(rule.getSystemId()).name(rule.getName()).build();
            rulesCache.put(rule.getSystemId(), rbacRepository.saveRule(dbRule));
          } else {
            if (!ruleOpt.get().getName().equals(rule.getName())) {
              ruleOpt.get().setName(rule.getName());
              rbacRepository.saveRule(ruleOpt.get());
            }
            rulesCache.put(rule.getSystemId(), ruleOpt.get());
          }
        });
  }

  private void storeRoles(final Map<String, CacheUser> users) {
    users.values().stream().map(CacheUser::getRoles).flatMap(List::stream).forEach(role -> {
      final Optional<Role> roleOpt = rbacRepository.getRoleBySystemId(role.getSystemId());
      if (!roleOpt.isPresent()) {
        final RoleBuilder roleBuilder =
            new RoleBuilder().systemId(role.getSystemId()).name(role.getName());
        role.getRules().stream().map(rule -> rulesCache.get(rule.getSystemId()))
            .forEach(rule -> roleBuilder.addRule(rule));
        rolesCache.put(role.getSystemId(), rbacRepository.saveRole(roleBuilder.build()));
      } else {
        updateRole(role, roleOpt);
      }
    });
  }

  private void updateRole(final CacheRole role, final Optional<Role> roleOpt) {
    final Role dbRole = roleOpt.get();
    if (!dbRole.getName().equals(role.getName())) {
      dbRole.setName(role.getName());
      rbacRepository.saveRole(dbRole);
    }

    boolean toUpdate = false;
    if (dbRole.getRules().size() != role.getRules().size()) {
      toUpdate = true;
    }
    if (!toUpdate) {
      final Map<String, Rule> dbRules =
          dbRole.getRules().stream().collect(Collectors.toMap(Rule::getSystemId, r -> r));
      if (role.getRules().stream().filter(rule -> !dbRules.containsKey(rule.getSystemId()))
          .count() >= 1) {
        toUpdate = true;
      }
    }

    if (toUpdate) {
      final List<Rule> newRules = role.getRules().stream()
          .map(rule -> rulesCache.get(rule.getSystemId())).collect(Collectors.toList());
      dbRole.setRules(newRules);
      rbacRepository.saveRole(dbRole);
    }

    rolesCache.put(dbRole.getSystemId(), dbRole);
  }

  private void storeUsers(final Map<String, CacheUser> users) {
    users.values().stream().forEach(user -> {
      final Optional<User> userOpt = rbacRepository.getUserBySystemId(user.getSystemId());
      if (!userOpt.isPresent()) {
        final UserBuilder userBuilder =
            new UserBuilder().systemId(user.getSystemId()).firstname(user.getFirstname())
                .lastname(user.getLastname()).username(user.getUsername());
        user.getRoles().stream().map(role -> rolesCache.get(role.getSystemId()))
            .forEach(role -> userBuilder.addRole(role));
        rbacRepository.saveUser(userBuilder.build());
      } else {
        updateUser(user, userOpt);
      }
    });
  }

  private void updateUser(final CacheUser cacheUser, final Optional<User> userOpt) {
    final User dbUser = userOpt.get();
    if (!dbUser.getFirstname().equals(cacheUser.getFirstname())
        || !dbUser.getLastname().equals(cacheUser.getLastname())) {
      dbUser.setFirstname(cacheUser.getFirstname());
      dbUser.setLastname(cacheUser.getLastname());
      rbacRepository.saveUser(dbUser);
    }

    boolean toUpdate = false;
    if (dbUser.getRoles().size() != cacheUser.getRoles().size()) {
      toUpdate = true;
    }

    final Map<String, Role> roles =
        dbUser.getRoles().stream().collect(Collectors.toMap(Role::getSystemId, r -> r));
    if (cacheUser.getRoles().stream().filter(role -> !roles.containsKey(role.getSystemId()))
        .count() >= 1) {
      toUpdate = true;
    }

    if (toUpdate) {
      final List<Role> newRoles = cacheUser.getRoles().stream()
          .map(role -> rolesCache.get(role.getSystemId())).collect(Collectors.toList());
      dbUser.setRoles(newRoles);
    }
  }
}
