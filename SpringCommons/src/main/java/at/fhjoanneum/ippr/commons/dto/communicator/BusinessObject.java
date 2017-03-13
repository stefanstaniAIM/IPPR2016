package at.fhjoanneum.ippr.commons.dto.communicator;

import java.io.Serializable;
import java.util.Set;

public class BusinessObject implements Serializable {

  private static final long serialVersionUID = 1L;

  private String name;

  private Set<BusinessObjectField> fields;

  public BusinessObject() {}

  public BusinessObject(final String name, final Set<BusinessObjectField> fields) {
    this.name = name;
    this.fields = fields;
  }

  public String getName() {
    return name;
  }

  public Set<BusinessObjectField> getFields() {
    return fields;
  }
}
