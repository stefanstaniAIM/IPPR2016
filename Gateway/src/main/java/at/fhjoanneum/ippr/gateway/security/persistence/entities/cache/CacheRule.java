package at.fhjoanneum.ippr.gateway.security.persistence.entities.cache;

public class CacheRule {

  private final String systemId;
  private final String name;

  public CacheRule(final String systemId, final String name) {
    this.systemId = systemId;
    this.name = name;
  }

  public String getSystemId() {
    return systemId;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null) {
      return false;
    }
    if (!CacheRule.class.isAssignableFrom(obj.getClass())) {
      return false;
    }
    final CacheRule other = (CacheRule) obj;
    if ((this.systemId == null) ? (other.getSystemId() != null)
        : !this.systemId.equals(other.getSystemId())) {
      return false;
    }
    return true;
  }
}
