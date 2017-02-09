package at.fhjoanneum.ippr.commons.dto.owlimport.jsonimport;

import java.io.Serializable;
import java.util.List;

public class ImportBusinessObjectModelDTO implements Serializable {

  private static final long serialVersionUID = 4414449659321550987L;

  private String id;
  private String name;
  private List<String> stateIds;

  public ImportBusinessObjectModelDTO() {}

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
