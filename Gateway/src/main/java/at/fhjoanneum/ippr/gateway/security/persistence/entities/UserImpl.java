package at.fhjoanneum.ippr.gateway.security.persistence.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import at.fhjoanneum.ippr.gateway.security.persistence.objects.Group;
import at.fhjoanneum.ippr.gateway.security.persistence.objects.User;

@Entity(name = "USER")
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

  @Column
  private String email;

  @ManyToMany
  @JoinTable(name = "user_group_map", joinColumns = {@JoinColumn(name = "u_id")},
      inverseJoinColumns = {@JoinColumn(name = "g_id")})
  private List<GroupImpl> groups = Lists.newArrayList();

  UserImpl() {}

  UserImpl(final String firstname, final String lastname, final String email,
      final List<GroupImpl> groups, final String systemId) {
    this.firstname = firstname;
    this.lastname = lastname;
    this.email = email;
    this.groups = groups;
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
  public String getLastname() {
    return lastname;
  }

  @Override
  public String getEmail() {
    return email;
  }

  @Override
  public List<Group> getGroups() {
    return ImmutableList.copyOf(groups);
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
        .append("lastname", lastname).append("firstname", firstname).append("groups", groups)
        .toString();
  }
}
