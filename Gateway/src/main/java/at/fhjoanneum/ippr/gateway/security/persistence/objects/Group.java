package at.fhjoanneum.ippr.gateway.security.persistence.objects;

import java.util.List;

public interface Group {
  Long getGId();

  String getName();

  List<User> getUser();
}
