package at.fhjoanneum.ippr.communicator.persistence.objects.internal;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class InternalObject implements Serializable {

  @JsonIgnore
  private static final long serialVersionUID = 1L;

  private String name;
  private Map<String, InternalField> fields = new HashMap<>();

  public InternalObject() {}

  public InternalObject(final String name, final Map<String, InternalField> fields) {
    this.name = name;
    this.fields = fields;
  }

  public String getName() {
    return name;
  }

  public Map<String, InternalField> getFields() {
    return fields;
  }

  @Override
  public String toString() {
    return "InternalObject [name=" + name + ", fields=" + fields + "]";
  }
}
