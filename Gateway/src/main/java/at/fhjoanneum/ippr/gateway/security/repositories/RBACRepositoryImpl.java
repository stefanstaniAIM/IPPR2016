package at.fhjoanneum.ippr.gateway.security.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;

import at.fhjoanneum.ippr.gateway.security.persistence.entities.RoleImpl;
import at.fhjoanneum.ippr.gateway.security.persistence.entities.RuleImpl;
import at.fhjoanneum.ippr.gateway.security.persistence.entities.UserImpl;
import at.fhjoanneum.ippr.gateway.security.persistence.objects.Role;
import at.fhjoanneum.ippr.gateway.security.persistence.objects.Rule;
import at.fhjoanneum.ippr.gateway.security.persistence.objects.User;

@Repository
public class RBACRepositoryImpl implements RBACRepository {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private RuleRepository ruleRepository;

  @Override
  public User saveUser(final User user) {
    return userRepository.save((UserImpl) user);
  }

  @Override
  public Role saveRole(final Role group) {
    return roleRepository.save((RoleImpl) group);
  }

  @Override
  public Rule saveRule(final Rule rule) {
    return ruleRepository.save((RuleImpl) rule);
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
  public List<User> getUsersByRoleName(final String roleName) {
    return Lists.newArrayList(userRepository.findByRoleName(roleName));
  }

  @Override
  public Optional<Role> getRoleByRoleName(final String roleName) {
    return Optional.ofNullable(roleRepository.findByRoleName(roleName));
  }

  @Override
  public Optional<Role> getRoleBySystemId(final String systemId) {
    return Optional.ofNullable(roleRepository.findBySystemId(systemId));
  }

  @Override
  public Optional<Rule> getRuleBySystemId(final String systemId) {
    return Optional.ofNullable(ruleRepository.findBySystemId(systemId));
  }

  @Override
  public List<User> getUsersByRuleNames(final List<String> ruleNames) {
    return Lists.newArrayList(userRepository.findByRuleNames(ruleNames));
  }

  @Override
  public List<Rule> getRules() {
    return Lists.newArrayList(ruleRepository.findAll());
  }

  interface UserRepository extends PagingAndSortingRepository<UserImpl, Long> {

    @Query(value = "SELECT * FROM USER WHERE SYSTEM_ID = :systemId", nativeQuery = true)
    UserImpl findBySystemId(@Param("systemId") String systemId);

    @Query(value = "SELECT * FROM USER WHERE USERNAME = :username", nativeQuery = true)
    UserImpl findByUsername(@Param("username") String username);

    @Query(
        value = "select u.* from user u join user_role_map ugm on ugm.u_id = u.u_id "
            + "join role r on r.role_id = ugm.role_id " + "where lower(r.name) = lower(:roleName) ",
        nativeQuery = true)
    List<UserImpl> findByRoleName(@Param("roleName") String roleName);

    @Query(
        value = "select distinct(u.u_id), u.* from rule join role_rule_map rrm on rrm.rule_id = rule.rule_id "
            + "join user_role_map urm on urm.role_id = rrm.role_id "
            + "join user u on urm.u_id = u.u_id where rule.name in ( :ruleNames )",
        nativeQuery = true)
    List<UserImpl> findByRuleNames(@Param("ruleNames") List<String> ruleNames);
  }

  interface RoleRepository extends PagingAndSortingRepository<RoleImpl, Long> {

    @Query(value = "SELECT * FROM ROLE WHERE SYSTEM_ID = :systemId", nativeQuery = true)
    RoleImpl findBySystemId(@Param("systemId") String systemId);

    @Query(value = "SELECT * FROM ROLE WHERE lower(NAME) = lower(:roleName)", nativeQuery = true)
    RoleImpl findByRoleName(@Param("roleName") String roleName);
  }

  interface RuleRepository extends CrudRepository<RuleImpl, Long> {

    @Query(value = "SELECT * FROM RULE WHERE SYSTEM_ID = :systemId", nativeQuery = true)
    RuleImpl findBySystemId(@Param("systemId") String systemId);
  }
}
