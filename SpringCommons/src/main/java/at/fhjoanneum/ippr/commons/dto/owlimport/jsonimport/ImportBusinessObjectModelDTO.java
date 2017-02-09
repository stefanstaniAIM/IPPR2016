package at.fhjoanneum.ippr.commons.dto.owlimport.jsonimport;

import java.io.Serializable;
import java.util.List;

public class ImportBusinessObjectModelDTO implements Serializable {

  private static final long serialVersionUID = 4414449659321550987L;

  private String id;
  private String name;
  private List<String> stateIds;

  public ImportBusinessObjectModelDTO() {}

  public ImportBusinessObjectModelDTO(final String id, final String name,
      final List<String> stateIds) {
    this.id = id;
    this.name = name;
    this.stateIds = stateIds;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public List<String> getStateIds() {
    return stateIds;
  }
}
