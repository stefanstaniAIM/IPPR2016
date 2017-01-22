package at.fhjoanneum.ippr.gateway.security.persistence.objects;

import java.util.List;

public interface Role {

  Long getRoleId();

  String getSystemId();

  String getName();

  List<Rule> getRules();

  void setRules(List<Rule> rules);

  void setName(String name);
}
