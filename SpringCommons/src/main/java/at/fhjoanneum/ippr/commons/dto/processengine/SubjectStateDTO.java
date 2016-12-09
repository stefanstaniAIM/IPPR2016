package at.fhjoanneum.ippr.commons.dto.processengine;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SubjectStateDTO implements UserContainer, Serializable {

  private static final long serialVersionUID = 2909869639424814690L;

  private Long ssId;
  private Long userId;
  private String subjectName;
  private String stateName;
  private String stateFunctionType;
  private String receiveState;
  private LocalDateTime lastChanged;
  private UserDTO user;


  public SubjectStateDTO() {}

  public SubjectStateDTO(final Long ssId, final Long userId, final String subjectName,
      final String stateName, final String stateFunctionType, final String receiveState,
      final LocalDateTime lastChanged) {
    this.ssId = ssId;
    this.userId = userId;
    this.subjectName = subjectName;
    this.stateName = stateName;
    this.stateFunctionType = stateFunctionType;
    this.receiveState = receiveState;
    this.lastChanged = lastChanged;
  }

  public Long getSsId() {
    return ssId;
  }

  @Override
  public Long getUserId() {
    return userId;
  }

  public String getSubjectName() {
    return subjectName;
  }

  public String getStateName() {
    return stateName;
  }

  public String getStateFunctionType() {
    return stateFunctionType;
  }

  public String getReceiveState() {
    return receiveState;
  }

  public LocalDateTime getLastChanged() {
    return lastChanged;
  }

  @Override
  public void appendUser(final UserDTO user) {
    this.user = user;
  }

  @Override
  public UserDTO getUser() {
    return user;
  }
}
