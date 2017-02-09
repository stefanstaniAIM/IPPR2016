package at.fhjoanneum.ippr.commons.dto.owlimport.jsonimport;

import java.io.Serializable;

public class ImportBusinessObjectFieldsModelDTO implements Serializable {

  private static final long serialVersionUID = 1220252083230351590L;

  private String id;
  private String name;
  private String bomId;
  private String type;

  public ImportBusinessObjectFieldsModelDTO() {}

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getBomId() {
    return bomId;
  }

  public String getType() {
    return type;
  }
}
