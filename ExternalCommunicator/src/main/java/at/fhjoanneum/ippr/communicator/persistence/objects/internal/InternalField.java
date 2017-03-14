package at.fhjoanneum.ippr.communicator.persistence.objects.internal;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import at.fhjoanneum.ippr.communicator.persistence.objects.DataType;

public class InternalField implements Serializable {

  @JsonIgnore
  private static final long serialVersionUID = 1L;

  private String name;
  private DataType dataType;
  private String value;

  public InternalField() {}

  public InternalField(final String name, final DataType dataType, final String value) {
    this.name = name;
    this.dataType = dataType;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public DataType getDataType() {
    return dataType;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "InternalField [name=" + name + ", dataType=" + dataType + ", value=" + value + "]";
  }
}
