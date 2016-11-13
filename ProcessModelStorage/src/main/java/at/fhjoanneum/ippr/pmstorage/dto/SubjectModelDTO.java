package at.fhjoanneum.ippr.pmstorage.dto;

public class SubjectModelDTO {

  private final Long smId;
  private final String name;
  private final String group;

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
