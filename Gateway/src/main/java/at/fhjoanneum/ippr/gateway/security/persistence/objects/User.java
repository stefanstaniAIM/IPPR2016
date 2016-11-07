package at.fhjoanneum.ippr.gateway.security.persistence.objects;

import java.util.List;

public interface User {

  Long getUId();

  String getSystemId();

  String getFirstname();

  String getLastname();

  String getEmail();

  List<Group> getGroups();
}
