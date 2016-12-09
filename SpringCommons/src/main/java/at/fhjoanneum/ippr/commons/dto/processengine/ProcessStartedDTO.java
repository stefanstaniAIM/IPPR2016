package at.fhjoanneum.ippr.commons.dto.processengine;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProcessStartedDTO implements Serializable {

  private static final long serialVersionUID = -1669494957249466855L;

  private Long piId;
  private String error;

  public ProcessStartedDTO() {}

  public ProcessStartedDTO(final Long piId, final String error) {
    this.piId = piId;
    this.error = error;
  }

  public Long getPiId() {
    return piId;
  }

  public String getError() {
    return error;
  }
}
