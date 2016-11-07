package at.fhjoanneum.ippr.gateway.security.persistence.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import at.fhjoanneum.ippr.gateway.security.persistence.objects.Group;
import at.fhjoanneum.ippr.gateway.security.persistence.objects.User;

@Entity(name = "USER_GROUP")
public class GroupImpl implements Group, Serializable {

  private static final long serialVersionUID = -3752242631499306265L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long gId;

  @Column
  @NotBlank
  private String name;

  @ManyToMany(mappedBy = "groups")
  private final List<UserImpl> users = Lists.newArrayList();

  GroupImpl() {}

  GroupImpl(final String name) {
    this.name = name;
  }

  @Override
  public Long getGId() {
    return gId;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public List<User> getUser() {
    return ImmutableList.copyOf(users);
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null) {
      return false;
    }
    if (!Group.class.isAssignableFrom(obj.getClass())) {
      return false;
    }
    final Group other = (Group) obj;
    if ((this.gId == null) ? (other.getGId() != null) : !this.gId.equals(other.getGId())) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(gId);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("gId", gId)
        .append("name", name).toString();
  }
}
