package at.fhjoanneum.ippr.gateway.security.persistence.entities;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
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
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import at.fhjoanneum.ippr.gateway.security.persistence.objects.Role;
import at.fhjoanneum.ippr.gateway.security.persistence.objects.Rule;
import at.fhjoanneum.ippr.gateway.security.persistence.objects.User;

@Entity(name = "USER")
@XmlRootElement
public class UserImpl implements User, Serializable {

  private static final long serialVersionUID = -1301447708340607361L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long uId;

  @Column(unique = true)
  private String systemId;

  @Column
  @NotBlank
  private String firstname;

  @Column
  @NotBlank
  private String lastname;

  @Column(unique = true)
  private String username;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "user_role_map", joinColumns = {@JoinColumn(name = "u_id")},
      inverseJoinColumns = {@JoinColumn(name = "role_id")})
  private List<RoleImpl> roles = Lists.newArrayList();

  UserImpl() {}

  UserImpl(final String firstname, final String lastname, final String username,
      final List<RoleImpl> roles, final String systemId) {
    this.firstname = firstname;
    this.lastname = lastname;
    this.username = username;
    this.roles = roles;
    this.systemId = systemId;
  }

  @Override
  public Long getUId() {
    return uId;
  }

  @Override
  public String getSystemId() {
    return systemId;
  }

  @Override
  public String getFirstname() {
    return firstname;
  }

  @Override
  public void setFirstname(final String firstname) {
    checkArgument(StringUtils.isNotBlank(firstname));
    this.firstname = firstname;
  }

  @Override
  public String getLastname() {
    return lastname;
  }

  @Override
  public void setLastname(final String lastname) {
    checkArgument(StringUtils.isNotBlank(lastname));
    this.lastname = lastname;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public Set<Role> getRoles() {
    return ImmutableSet.copyOf(roles);
  }

  @Override
  public void setRoles(final List<Role> roles) {
    checkNotNull(roles);
    this.roles.clear();
    this.roles = roles.stream().filter(group -> group instanceof RoleImpl)
        .map(group -> (RoleImpl) group).collect(Collectors.toList());
  }

  @Override
  public Set<Rule> getRules() {
    return ImmutableSet.copyOf(roles.stream().map(Role::getRules).flatMap(List::stream)
        .map(rule -> rule).collect(Collectors.toSet()));
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null) {
      return false;
    }
    if (!User.class.isAssignableFrom(obj.getClass())) {
      return false;
    }
    final User other = (User) obj;
    if ((this.uId == null) ? (other.getUId() != null) : !this.uId.equals(other.getUId())) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(uId);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("uId", uId)
        .append("lastname", lastname).append("firstname", firstname).append("groups", roles)
        .toString();
  }
}
