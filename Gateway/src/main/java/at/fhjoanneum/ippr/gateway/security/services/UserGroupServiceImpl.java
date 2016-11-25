package at.fhjoanneum.ippr.gateway.security.services;

import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import at.fhjoanneum.ippr.commons.dto.user.UserDTO;
import at.fhjoanneum.ippr.gateway.security.persistence.objects.User;
import at.fhjoanneum.ippr.gateway.security.repositories.UserGroupRepository;

@Service
public class UserGroupServiceImpl implements UserGroupService {

  @Autowired
  private UserGroupRepository userGroupRepository;

  @Override
  public User getUserByUserId(final Long uId) {
    return userGroupRepository.getUserByUserId(uId).get();
  }

  @Async
  @Override
  public Future<List<UserDTO>> getPossibleUsersOfGroup(final String groupname) {
    final List<User> users = userGroupRepository.getUsersByGroupName(groupname);
    return new AsyncResult<List<UserDTO>>(users.stream()
        .map(user -> new UserDTO(user.getUId(), user.getFirstname(), user.getLastname(), groupname))
        .collect(Collectors.toList()));
  }
}
