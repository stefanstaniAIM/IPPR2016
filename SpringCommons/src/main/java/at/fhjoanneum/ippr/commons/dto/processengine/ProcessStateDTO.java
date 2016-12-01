package at.fhjoanneum.ippr.commons.dto.processengine;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProcessStateDTO implements Serializable {

  private static final long serialVersionUID = 6802550001350674258L;

  private Long piId;
  private String status;
  private List<SubjectStateDTO> subjects;

  public ProcessStateDTO() {}

  public ProcessStateDTO(final Long piId, final String status,
      final List<SubjectStateDTO> subjects) {
    this.piId = piId;
    this.status = status;
    this.subjects = subjects;
  }

  public Long getPiId() {
    return piId;
  }

  public String getStatus() {
    return status;
  }

  public List<SubjectStateDTO> getSubjects() {
    return subjects;
  }
}
