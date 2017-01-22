package at.fhjoanneum.ippr.gateway.security.persistence.objects;

public interface Rule {

  Long getRuleId();

  String getSystemId();

  String getName();

  void setName(String name);

}
