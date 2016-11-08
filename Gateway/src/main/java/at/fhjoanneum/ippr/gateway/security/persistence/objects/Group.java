package at.fhjoanneum.ippr.gateway.security.persistence.objects;

import java.util.List;

public interface Group {
  Long getGId();

  String getSystemId();

  String getName();

  void setName(String name);

  List<User> getUser();
}
