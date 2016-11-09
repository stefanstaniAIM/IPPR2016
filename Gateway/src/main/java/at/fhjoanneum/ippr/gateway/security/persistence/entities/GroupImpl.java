package at.fhjoanneum.ippr.gateway.security.persistence.entities;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

import com.google.common.base.Objects;

import at.fhjoanneum.ippr.gateway.security.persistence.objects.Group;

@Entity(name = "USER_GROUP")
@XmlRootElement
public class GroupImpl implements Group, Serializable {

  private static final long serialVersionUID = -3752242631499306265L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long gId;

  @Column
  @NotBlank
  private String name;

  @Column(unique = true)
  @NotBlank
  private String systemId;

  GroupImpl() {}

  GroupImpl(final String systemId, final String name) {
    this.systemId = systemId;
    this.name = name;
  }

  @Override
  public Long getGId() {
    return gId;
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
  public void setName(final String name) {
    checkArgument(StringUtils.isNotBlank(name));
    this.name = name;
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
