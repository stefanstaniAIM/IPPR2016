package at.fhjoanneum.ippr.commons.dto.pmstorage;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SubjectModelDTO {

  private Long smId;
  private String name;
  private List<String> assignedRules;

  public SubjectModelDTO() {}

  public SubjectModelDTO(final Long smId, final String name, final List<String> assignedRules) {
    this.smId = smId;
    this.name = name;
    this.assignedRules = assignedRules;
  }

  public Long getSmId() {
    return smId;
  }

  public String getName() {
    return name;
  }

  public List<String> getAssignedRules() {
    return assignedRules;
  }
}
