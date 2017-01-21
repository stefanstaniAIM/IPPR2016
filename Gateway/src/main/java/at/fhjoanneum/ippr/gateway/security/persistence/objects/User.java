package at.fhjoanneum.ippr.gateway.security.persistence.objects;

import java.util.List;

public interface User {

  Long getUId();

  String getSystemId();

  String getFirstname();

  void setFirstname(String firstname);

  String getLastname();

  void setLastname(String lastname);

  String getUsername();

  List<Role> getRoles();

  List<Rule> getRules();

  void setRoles(List<Role> groups);
}
