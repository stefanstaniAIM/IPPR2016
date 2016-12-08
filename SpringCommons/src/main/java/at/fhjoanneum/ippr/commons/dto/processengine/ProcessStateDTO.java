package at.fhjoanneum.ippr.commons.dto.processengine;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProcessStateDTO implements Serializable {

  private static final long serialVersionUID = 6802550001350674258L;

  private Long piId;
  private String status;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private List<SubjectStateDTO> subjects;
  private String processName;

  public ProcessStateDTO() {}

  public ProcessStateDTO(final Long piId, final String status, final LocalDateTime startTime,
      final LocalDateTime endTime, final List<SubjectStateDTO> subjects, final String processName) {
    this.piId = piId;
    this.status = status;
    this.startTime = startTime;
    this.endTime = endTime;
    this.subjects = subjects;
    this.processName = processName;
  }

  public Long getPiId() {
    return piId;
  }

  public String getStatus() {
    return status;
  }

  public LocalDateTime getStartTime() {
    return startTime;
  }

  public LocalDateTime getEndTime() {
    return endTime;
  }

  public List<SubjectStateDTO> getSubjects() {
    return subjects;
  }

  public String getProcessName() {
    return processName;
  }
}
