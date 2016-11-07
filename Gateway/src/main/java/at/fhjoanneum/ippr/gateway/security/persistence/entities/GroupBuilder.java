package at.fhjoanneum.ippr.gateway.security.persistence.entities;

import static com.google.common.base.Preconditions.checkArgument;

import org.apache.commons.lang3.StringUtils;

import at.fhjoanneum.ippr.gateway.security.persistence.Builder;
import at.fhjoanneum.ippr.gateway.security.persistence.objects.Group;

public class GroupBuilder implements Builder<Group> {

  private String name;

  GroupBuilder name(final String name) {
    checkArgument(StringUtils.isNotBlank(name));
    this.name = name;
    return this;
  }

  @Override
  public Group build() {
    checkArgument(StringUtils.isNotBlank(name));
    return new GroupImpl(name);
  }


}
