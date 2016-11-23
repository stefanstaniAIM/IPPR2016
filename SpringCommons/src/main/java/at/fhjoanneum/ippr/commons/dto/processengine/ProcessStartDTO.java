package at.fhjoanneum.ippr.commons.dto.processengine;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProcessStartDTO implements Serializable {

  private Long pmId;
  private List<UserGroupAssignmentDTO> assignments;

  public ProcessStartDTO() {}

  public Long getPmId() {
    return pmId;
  }

  public List<UserGroupAssignmentDTO> getAssignments() {
    return assignments;
  }
}
