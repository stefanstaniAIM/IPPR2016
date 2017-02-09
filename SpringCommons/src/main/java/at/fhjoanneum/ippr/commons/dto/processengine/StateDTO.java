package at.fhjoanneum.ippr.commons.dto.processengine;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.base.Objects;

@XmlRootElement
public class StateDTO implements Serializable {

  private static final long serialVersionUID = -8351022484341633532L;

  private Long nextStateId;
  private String name;
  private Boolean endState;

  public StateDTO() {}

  public StateDTO(final Long sId, final String name, final Boolean endState) {
    this.nextStateId = sId;
    this.name = name;
    this.endState = endState;
  }

  public String getName() {
    return name;
  }

  public Long getNextStateId() {
    return nextStateId;
  }

  public Boolean isEndState() {
    return endState;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(nextStateId);
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final StateDTO other = (StateDTO) obj;
    if (nextStateId == null) {
      if (other.nextStateId != null)
        return false;
    } else if (!nextStateId.equals(other.nextStateId))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("sId", getNextStateId()).append("name", getName()).toString();
  }
}
