package at.fhjoanneum.ippr.commons.dto.processengine;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class TaskDTO implements Serializable {

  private static final long serialVersionUID = 6523560389089371205L;

  private Long piId;
  private String processName;
  private Long ssId;
  private String stateName;
  private String functionType;
  private LocalDateTime lastChanged;

  public TaskDTO() {}

  public TaskDTO(final Long piId, final String processName, final Long ssId, final String stateName,
      final String functionType, final LocalDateTime lastChanged) {
    this.piId = piId;
    this.processName = processName;
    this.ssId = ssId;
    this.stateName = stateName;
    this.functionType = functionType;
    this.lastChanged = lastChanged;
  }

  public Long getPiId() {
    return piId;
  }

  public String getProcessName() {
    return processName;
  }

  public Long getSsId() {
    return ssId;
  }

  public String getStateName() {
    return stateName;
  }

  public String getFunctionType() {
    return functionType;
  }

  public LocalDateTime getLastChanged() {
    return lastChanged;
  }
}
