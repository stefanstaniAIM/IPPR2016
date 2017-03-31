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


  @Override
  public String toString() {
    return "BusinessObject [name=" + name + ", fields=" + fields + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final BusinessObject other = (BusinessObject) obj;
    if (name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!name.equals(other.name)) {
      return false;
    }
    return true;
  }
}
