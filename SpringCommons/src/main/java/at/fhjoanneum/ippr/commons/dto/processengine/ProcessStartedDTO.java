package at.fhjoanneum.ippr.commons.dto.processengine;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProcessStartedDTO {
  private final Long piId;
  private final String error;

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
