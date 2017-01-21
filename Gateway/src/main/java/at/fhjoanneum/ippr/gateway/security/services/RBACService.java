package at.fhjoanneum.ippr.gateway.security.services;

import java.util.List;
import java.util.concurrent.Future;

import at.fhjoanneum.ippr.commons.dto.user.UserDTO;
import at.fhjoanneum.ippr.gateway.security.persistence.objects.User;

public interface RBACService {

  public User getUserByUserId(Long uId);

  public Future<List<UserDTO>> getPossibleUsersOfGroup(String group);

  public Future<List<UserDTO>> getUsersOfRule(List<String> ruleNames);
}
