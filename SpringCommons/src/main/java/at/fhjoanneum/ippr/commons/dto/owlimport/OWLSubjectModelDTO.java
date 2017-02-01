package at.fhjoanneum.ippr.commons.dto.owlimport;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement
public class OWLSubjectModelDTO implements Serializable {

  private String name;

  public OWLSubjectModelDTO() {}

  public OWLSubjectModelDTO(final String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
