package at.fhjoanneum.ippr.commons.dto.owlimport.reader;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement
public class OWLSubjectModelDTO implements Serializable {

  private String id;
  private String name;

  public OWLSubjectModelDTO() {}

  public OWLSubjectModelDTO(final String id, final String name) {
    this.id = id;
    this.name = name;
  }

  public String getName() {
    return name;
  }
  public String getId() {
    return id;
  }
}
