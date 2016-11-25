package at.fhjoanneum.ippr.commons.dto.user;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GroupDTO {

  private String groupname;
  private Long groupId;

  public GroupDTO() {}

  public GroupDTO(final Long groupId, final String groupname) {
    this.groupname = groupname;
    this.groupId = groupId;
  }

  public Long getGroupId() {
    return groupId;
  }

  public String getGroupname() {
    return groupname;
  }
}
