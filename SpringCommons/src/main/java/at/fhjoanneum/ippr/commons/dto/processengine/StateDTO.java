package at.fhjoanneum.ippr.commons.dto.processengine;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StateDTO implements Serializable {

  private static final long serialVersionUID = -8351022484341633532L;

  private Long sId;
  private String name;

  public StateDTO() {}

  public StateDTO(final Long sId, final String name) {
    this.sId = sId;
    this.name = name;
  }

  public Long getSId() {
    return sId;
  }

  public String getName() {
    return name;
  }
}
