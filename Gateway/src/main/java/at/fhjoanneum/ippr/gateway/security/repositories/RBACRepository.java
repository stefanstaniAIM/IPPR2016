package at.fhjoanneum.ippr.gateway.security.repositories;

import java.util.List;
import java.util.Optional;

import at.fhjoanneum.ippr.gateway.security.persistence.objects.Role;
import at.fhjoanneum.ippr.gateway.security.persistence.objects.Rule;
import at.fhjoanneum.ippr.gateway.security.persistence.objects.User;

public interface RBACRepository {

  User saveUser(final User user);

  Role saveRole(final Role group);

  Rule saveRule(final Rule rule);

  Optional<User> getUserByUserId(final Long userId);

  Optional<User> getUserBySystemId(final String systemId);

  Optional<User> getUserByUsername(final String username);

  Optional<Rule> getRuleBySystemId(final String systemId);

  Optional<Role> getRoleBySystemId(final String systemId);

  Optional<Role> getRoleByRoleName(final String roleName);

  List<User> getUsersByRoleName(final String roleName);

  List<User> getUsersByRuleNames(final List<String> ruleNames);

  List<Rule> getRules();
}
