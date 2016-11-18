package at.fhjoanneum.ippr.commons.dto.processengine;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProcessStartDTO {

  private Long pmId;
  private List<UserGroupAssignment> assignments;

  public ProcessStartDTO() {}

  public Long getPmId() {
    return pmId;
  }

  public List<UserGroupAssignment> getAssignments() {
    return assignments;
  }
}
