package at.fhjoanneum.ippr.commons.dto.owlimport.jsonimport;

import java.io.Serializable;
import java.util.List;

public class ImportSubjectModelDTO implements Serializable {

  private static final long serialVersionUID = 1482443486634109836L;

  private String id;
  private String name;
  private List<String> assignedRules;

  public ImportSubjectModelDTO() {}

  public ImportSubjectModelDTO(final String id, final String name,
      final List<String> assignedRules) {
    this.id = id;
    this.name = name;
    this.assignedRules = assignedRules;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public List<String> getAssignedRules() {
    return assignedRules;
  }


}
