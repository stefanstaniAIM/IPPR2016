package at.fhjoanneum.ippr.commons.dto.processengine;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlRootElement;

import at.fhjoanneum.ippr.commons.dto.processengine.user.UserContainer;
import at.fhjoanneum.ippr.commons.dto.processengine.user.UserDTO;

@XmlRootElement
public class ProcessInfoDTO implements UserContainer, Serializable {

  private static final long serialVersionUID = 4432720548280531738L;

  private Long piId;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private String processName;
  private Long userId;
  private UserDTO user;
  private String state;

  public ProcessInfoDTO() {}

  public ProcessInfoDTO(final Long piId, final LocalDateTime startTime, final LocalDateTime endTime,
      final String processName, final Long startUserId) {
    this.piId = piId;
    this.startTime = startTime;
    this.endTime = endTime;
    this.processName = processName;
    this.userId = startUserId;
  }

  public ProcessInfoDTO(final Long piId, final LocalDateTime startTime, final LocalDateTime endTime,
      final String processName, final Long startUserId, final String state) {
    this(piId, startTime, endTime, processName, startUserId);
    this.state = state;
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

  @Override
  public void appendUser(final UserDTO user) {
    this.user = user;
  }

  @Override
  public UserDTO getUser() {
    return user;
  }

  @Override
  public Long getUserId() {
    return userId;
  }

  public String getState() {
    return state;
  }
}
