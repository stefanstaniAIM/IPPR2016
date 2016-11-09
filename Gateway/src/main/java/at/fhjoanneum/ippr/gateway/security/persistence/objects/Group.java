package at.fhjoanneum.ippr.gateway.security.persistence.objects;

public interface Group {
  Long getGId();

  String getSystemId();

  String getName();

  void setName(String name);
}
