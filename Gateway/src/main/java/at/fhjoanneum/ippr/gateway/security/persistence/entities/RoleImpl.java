package at.fhjoanneum.ippr.gateway.security.persistence.entities;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import at.fhjoanneum.ippr.gateway.security.persistence.objects.Role;
import at.fhjoanneum.ippr.gateway.security.persistence.objects.Rule;

@Entity(name = "ROLE")
@XmlRootElement
public class RoleImpl implements Role, Serializable {

  private static final long serialVersionUID = -3752242631499306265L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long roleId;

  @Column
  @NotBlank
  private String name;

  @Column(unique = true)
  @NotBlank
  private String systemId;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "role_rule_map", joinColumns = {@JoinColumn(name = "role_id")},
      inverseJoinColumns = {@JoinColumn(name = "rule_id")})
  private List<RuleImpl> rules = Lists.newArrayList();

  RoleImpl() {}

  RoleImpl(final String systemId, final String name, final List<RuleImpl> rules) {
    this.systemId = systemId;
    this.name = name;
    this.rules = rules;
  }

  @Override
  public Long getRoleId() {
    return roleId;
  }

  @Override
  public String getSystemId() {
    return systemId;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public List<Rule> getRules() {
    return ImmutableList.copyOf(rules);
  }

  @Override
  public void setRules(final List<Rule> rules) {
    this.rules = rules.stream().map(rule -> (RuleImpl) rule).collect(Collectors.toList());
  }

  @Override
  public void setName(final String name) {
    checkArgument(StringUtils.isNotBlank(name));
    this.name = name;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null) {
      return false;
    }
    if (!Role.class.isAssignableFrom(obj.getClass())) {
      return false;
    }
    final Role other = (Role) obj;
    if ((this.roleId == null) ? (other.getRoleId() != null)
        : !this.roleId.equals(other.getRoleId())) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(roleId);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("roleId", roleId)
        .append("name", name).toString();
  }
}
