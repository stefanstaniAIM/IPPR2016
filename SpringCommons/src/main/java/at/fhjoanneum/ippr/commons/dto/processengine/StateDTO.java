package at.fhjoanneum.ippr.commons.dto.processengine;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@XmlRootElement
public class StateDTO implements Serializable {

  private static final long serialVersionUID = -8351022484341633532L;

  private Long nextStateId;
  private String name;
  private boolean isEndState;

  public StateDTO() {}

  public StateDTO(final Long sId, final String name, final boolean isEndState) {
    this.nextStateId = sId;
    this.name = name;
    this.isEndState = isEndState;
  }

  public String getName() {
    return name;
  }

  public Long getNextStateId() {
    return nextStateId;
  }

  public boolean isEndState() {
    return isEndState;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("sId", getNextStateId()).append("name", getName()).toString();
  }
}
