package at.fhjoanneum.ippr.gateway.security.persistence.entities.cache;

import static com.google.common.base.Preconditions.checkArgument;

import org.apache.commons.lang3.StringUtils;

public class CacheGroup {

  private final String systemId;
  private final String name;

  public CacheGroup(final String systemId, final String name) {
    checkArgument(StringUtils.isNotBlank(systemId));
    checkArgument(StringUtils.isNotBlank(name));
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
    if (!CacheGroup.class.isAssignableFrom(obj.getClass())) {
      return false;
    }
    final CacheGroup other = (CacheGroup) obj;
    if ((this.systemId == null) ? (other.getSystemId() != null)
        : !this.systemId.equals(other.getSystemId())) {
      return false;
    }
    return true;
  }
}
