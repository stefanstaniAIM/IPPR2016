package at.fhjoanneum.ippr.gateway.security.services;

import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import at.fhjoanneum.ippr.commons.dto.user.RoleDTO;
import at.fhjoanneum.ippr.commons.dto.user.RuleDTO;
import at.fhjoanneum.ippr.commons.dto.user.UserDTO;
import at.fhjoanneum.ippr.gateway.security.persistence.objects.Rule;
import at.fhjoanneum.ippr.gateway.security.persistence.objects.User;
import at.fhjoanneum.ippr.gateway.security.repositories.RBACRepository;

@Service
public class RBACServiceImpl implements RBACService {

  @Autowired
  private RBACRepository rbacRepository;

  @Override
  public User getUserByUserId(final Long uId) {
    return rbacRepository.getUserByUserId(uId).get();
  }

  @Async
  @Override
  public Future<List<UserDTO>> getUsersOfRule(final List<String> ruleNames) {
    final List<User> users = rbacRepository.getUsersByRuleNames(ruleNames);
    return new AsyncResult<List<UserDTO>>(users.stream().map(user -> {
      final List<RoleDTO> roles = user.getRoles().stream()
          .map(role -> new RoleDTO(role.getRoleId(), role.getName())).collect(Collectors.toList());
      final List<RuleDTO> rules = user.getRules().stream()
          .map(rule -> new RuleDTO(rule.getRuleId(), rule.getName())).collect(Collectors.toList());
      return new UserDTO(user.getUId(), user.getFirstname(), user.getLastname(), roles, rules);
    }).collect(Collectors.toList()));
  }

  @Override
  public Future<List<Rule>> getRules() {
    return new AsyncResult<List<Rule>>(rbacRepository.getRules());
  }
}
