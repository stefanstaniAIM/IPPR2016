package at.fhjoanneum.ippr.commons.dto.processengine.stateobject;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import at.fhjoanneum.ippr.commons.dto.processengine.StateDTO;

@XmlRootElement
public class StateObjectDTO implements Serializable {

  private static final long serialVersionUID = -1073231546568305460L;

  private Long piId;
  private Long ssId;
  private List<BusinessObjectDTO> businessObjects;
  private List<StateDTO> nextStates;
  private List<SubjectDTO> assignedUsers;

  public StateObjectDTO() {}

  public StateObjectDTO(final Long piId, final Long ssId,
      final List<BusinessObjectDTO> businessObjects, final List<StateDTO> nextStates) {
    this.piId = piId;
    this.ssId = ssId;
    this.businessObjects = businessObjects;
    this.nextStates = nextStates;
  }

  public StateObjectDTO(final Long piId, final Long ssId,
      final List<BusinessObjectDTO> businessObjects, final List<StateDTO> nextStates,
      final List<SubjectDTO> assignedUsers) {
    this(piId, ssId, businessObjects, nextStates);
    this.assignedUsers = assignedUsers;
  }

  public Long getPiId() {
    return piId;
  }

  public Long getSsId() {
    return ssId;
  }

  public List<BusinessObjectDTO> getBusinessObjects() {
    return businessObjects;
  }

  public List<StateDTO> getNextStates() {
    return nextStates;
  }

  public List<SubjectDTO> getAssignedUsers() {
    return assignedUsers;
  }
}
