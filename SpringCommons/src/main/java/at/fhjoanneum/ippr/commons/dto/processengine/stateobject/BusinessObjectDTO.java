package at.fhjoanneum.ippr.commons.dto.processengine.stateobject;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BusinessObjectDTO implements Serializable {

  private static final long serialVersionUID = -8170255134093311778L;

  private Long bomId;
  private Long boiId;
  private String name;
  private List<BusinessObjectFieldDTO> fields;
  private List<BusinessObjectDTO> children;

  public BusinessObjectDTO() {}


  public BusinessObjectDTO(final Long bomId, final Long boiId, final String name,
      final List<BusinessObjectFieldDTO> businessObjectFields) {
    this.bomId = bomId;
    this.boiId = boiId;
    this.name = name;
    this.fields = businessObjectFields;
  }

  public BusinessObjectDTO(final Long bomId, final Long boiId, final String name,
      final List<BusinessObjectFieldDTO> businessObjectFields,
      final List<BusinessObjectDTO> children) {
    this(bomId, boiId, name, businessObjectFields);
    this.children = children;
  }

  public Long getBomId() {
    return bomId;
  }

  public Long getBoiId() {
    return boiId;
  }

  public String getName() {
    return name;
  }

  public List<BusinessObjectFieldDTO> getFields() {
    return fields;
  }

  public List<BusinessObjectDTO> getChildren() {
    return children;
  }
}
