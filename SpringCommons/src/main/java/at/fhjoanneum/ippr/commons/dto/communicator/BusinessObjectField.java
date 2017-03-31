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

  @Override
  public String toString() {
    return "BusinessObjectField [name=" + name + ", type=" + type + ", value=" + value + "]";
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
    final BusinessObjectField other = (BusinessObjectField) obj;
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
