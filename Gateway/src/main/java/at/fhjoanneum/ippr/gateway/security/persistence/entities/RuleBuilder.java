package at.fhjoanneum.ippr.gateway.security.persistence.entities;

import static com.google.common.base.Preconditions.checkArgument;

import org.apache.commons.lang3.StringUtils;

import at.fhjoanneum.ippr.gateway.security.persistence.Builder;
import at.fhjoanneum.ippr.gateway.security.persistence.objects.Rule;

public class RuleBuilder implements Builder<Rule> {

  private String systemId;
  private String name;

  public RuleBuilder systemId(final String systemId) {
    checkArgument(StringUtils.isNotBlank(systemId));
    this.systemId = systemId;
    return this;
  }

  public RuleBuilder name(final String name) {
    checkArgument(StringUtils.isNotBlank(name));
    this.name = name;
    return this;
  }

  @Override
  public Rule build() {
    checkArgument(StringUtils.isNotBlank(systemId));
    checkArgument(StringUtils.isNotBlank(name));
    return new RuleImpl(name, systemId);
  }
}
