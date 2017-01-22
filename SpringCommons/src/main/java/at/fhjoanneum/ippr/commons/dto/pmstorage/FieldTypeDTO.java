package at.fhjoanneum.ippr.commons.dto.pmstorage;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FieldTypeDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private String type;

  public FieldTypeDTO() {}

  public FieldTypeDTO(final String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
