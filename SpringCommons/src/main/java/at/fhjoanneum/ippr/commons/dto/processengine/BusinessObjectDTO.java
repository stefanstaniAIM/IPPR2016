package at.fhjoanneum.ippr.commons.dto.processengine;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BusinessObjectDTO implements Serializable {

  private static final long serialVersionUID = -8170255134093311778L;

  private Long bomId;
  private Long boiId;
  private String name;
  private List<BusinessObjectFieldDTO> businessObjectFields;

  public BusinessObjectDTO() {}


  public BusinessObjectDTO(final Long bomId, final Long boiId, final String name,
      final List<BusinessObjectFieldDTO> businessObjectFields) {
    this.bomId = bomId;
    this.boiId = boiId;
    this.name = name;
    this.businessObjectFields = businessObjectFields;
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

  public List<BusinessObjectFieldDTO> getBusinessObjectFields() {
    return businessObjectFields;
  }
}
