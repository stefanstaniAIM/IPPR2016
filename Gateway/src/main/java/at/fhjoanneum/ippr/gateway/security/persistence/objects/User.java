package at.fhjoanneum.ippr.gateway.security.persistence.objects;

import java.util.List;
import java.util.Set;

public interface User {

  Long getUId();

  String getSystemId();

  String getFirstname();

  void setFirstname(String firstname);

  String getLastname();

  void setLastname(String lastname);

  String getUsername();

  Set<Role> getRoles();

  Set<Rule> getRules();

  void setRoles(List<Role> groups);
}
