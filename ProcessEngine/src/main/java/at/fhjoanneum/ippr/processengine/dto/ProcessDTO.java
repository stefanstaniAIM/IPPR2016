package at.fhjoanneum.ippr.processengine.dto;

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
