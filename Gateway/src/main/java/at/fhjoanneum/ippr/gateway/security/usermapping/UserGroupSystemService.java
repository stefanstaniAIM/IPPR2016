package at.fhjoanneum.ippr.gateway.security.usermapping;

import java.util.List;

import at.fhjoanneum.ippr.gateway.security.persistence.objects.User;

public interface UserGroupSystemService {

  List<User> getUsers();

  void mapUsers();
}
