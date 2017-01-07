package at.fhjoanneum.ippr.commons.dto.processengine;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StateDTO implements Serializable {

  private static final long serialVersionUID = -8351022484341633532L;

  private Long nextStateId;
  private String name;

  public StateDTO() {}

  public StateDTO(final Long sId, final String name) {
    this.nextStateId = sId;
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public Long getNextStateId() {
    return nextStateId;
  }
}
