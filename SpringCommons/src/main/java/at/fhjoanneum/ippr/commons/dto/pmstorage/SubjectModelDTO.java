package at.fhjoanneum.ippr.commons.dto.pmstorage;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SubjectModelDTO {

  private Long smId;
  private String name;
  private String group;

  public SubjectModelDTO() {}

  public SubjectModelDTO(final Long smId, final String name, final String group) {
    this.smId = smId;
    this.name = name;
    this.group = group;
  }

  public Long getSmId() {
    return smId;
  }

  public String getName() {
    return name;
  }

  public String getGroup() {
    return group;
  }


}
