package at.fhjoanneum.ippr.communicator.persistence.objects.internal;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;

@XmlRootElement
public class InternalData implements Serializable {

  @JsonIgnore
  private static final long serialVersionUID = 1L;

  private Map<String, InternalObject> objects = new HashMap<>();

  public InternalData() {}

  public InternalData(final Map<String, InternalObject> objects) {
    this.objects = objects;
  }

  public Map<String, InternalObject> getObjects() {
    return objects;
  }

  @Override
  public String toString() {
    return "InternalData [objects=" + objects + "]";
  }
}
