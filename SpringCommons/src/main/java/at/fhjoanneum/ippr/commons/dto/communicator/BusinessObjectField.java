package at.fhjoanneum.ippr.commons.dto.communicator;

import java.io.Serializable;

public class BusinessObjectField implements Serializable {

  private static final long serialVersionUID = 1L;

  private String name;
  private String type;
  private String value;

  public BusinessObjectField() {}

  public BusinessObjectField(final String name, final String type, final String value) {
    this.name = name;
    this.type = type;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public String getValue() {
    return value;
  }
}
