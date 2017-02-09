package at.fhjoanneum.ippr.commons.dto.owlimport.jsonimport;

import java.io.Serializable;

public class ImportBusinessObjectFieldsModelDTO implements Serializable {

  private static final long serialVersionUID = 1220252083230351590L;

  private String id;
  private String name;
  private String bomId;
  private String type;

  public ImportBusinessObjectFieldsModelDTO() {}

  public ImportBusinessObjectFieldsModelDTO(final String id, final String name, final String bomId,
      final String type) {
    this.id = id;
    this.name = name;
    this.bomId = bomId;
    this.type = type;
  }

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
