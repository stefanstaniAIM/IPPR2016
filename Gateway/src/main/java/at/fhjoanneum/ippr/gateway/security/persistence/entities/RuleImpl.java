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

import at.fhjoanneum.ippr.gateway.security.persistence.objects.Rule;

@Entity(name = "RULE")
@XmlRootElement
public class RuleImpl implements Rule, Serializable {

  private static final long serialVersionUID = 4352903292474094588L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long ruleId;

  @Column
  @NotBlank
  private String name;

  @Column(unique = true)
  @NotBlank
  private String systemId;

  public RuleImpl() {}

  public RuleImpl(final String name, final String systemId) {
    this.name = name;
    this.systemId = systemId;
  }

  @Override
  public Long getRuleId() {
    return ruleId;
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
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final RuleImpl other = (RuleImpl) obj;
    if (ruleId == null) {
      if (other.ruleId != null)
        return false;
    } else if (!ruleId.equals(other.ruleId))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(ruleId);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("ruleId", ruleId)
        .append("name", name).toString();
  }
}
