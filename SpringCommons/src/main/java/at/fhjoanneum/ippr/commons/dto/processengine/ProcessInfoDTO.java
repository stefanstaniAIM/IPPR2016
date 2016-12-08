package at.fhjoanneum.ippr.commons.dto.processengine;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProcessInfoDTO implements Serializable {

  private static final long serialVersionUID = 4432720548280531738L;

  private Long piId;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private String processName;
  private Long startUserId;

  public ProcessInfoDTO() {}

  public ProcessInfoDTO(final Long piId, final LocalDateTime startTime, final LocalDateTime endTime,
      final String processName, final Long startUserId) {
    this.piId = piId;
    this.startTime = startTime;
    this.endTime = endTime;
    this.processName = processName;
    this.startUserId = startUserId;
  }

  public Long getPiId() {
    return piId;
  }

  public LocalDateTime getStartTime() {
    return startTime;
  }

  public LocalDateTime getEndTime() {
    return endTime;
  }

  public String getProcessName() {
    return processName;
  }

  public Long getStartUserId() {
    return startUserId;
  }
}
