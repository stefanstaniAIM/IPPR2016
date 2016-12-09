package at.fhjoanneum.ippr.commons.dto.processengine;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProcessDTO implements Serializable {

  private static final long serialVersionUID = 2075853829794256949L;

  private Long piId;

  public ProcessDTO() {}

  public ProcessDTO(final Long piId) {
    this.piId = piId;
  }

  public Long getPiId() {
    return piId;
  }
}
