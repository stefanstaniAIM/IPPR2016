package at.fhjoanneum.ippr.commons.dto.processengine;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProcessDTO {

  private Long processId;

  public ProcessDTO() {}

  public ProcessDTO(final Long processId) {
    this.processId = processId;
  }

  public Long getProcessId() {
    return processId;
  }
}
