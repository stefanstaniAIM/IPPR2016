package at.fhjoanneum.ippr.commons.dto.processengine;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BusinessObjectDTO implements Serializable {

  private static final long serialVersionUID = -8170255134093311778L;

  private Long bomId;
  private Long boiId;
  private List<BusinessObjectFieldInstanceDTO> businessObjectFields;

  public BusinessObjectDTO() {}


  public BusinessObjectDTO(final Long bomId, final Long boiId,
      final List<BusinessObjectFieldInstanceDTO> businessObjectFields) {
    this.bomId = bomId;
    this.boiId = boiId;
    this.businessObjectFields = businessObjectFields;
  }

  public Long getBomId() {
    return bomId;
  }

  public Long getBoiId() {
    return boiId;
  }

  public List<BusinessObjectFieldInstanceDTO> getBusinessObjectFields() {
    return businessObjectFields;
  }
}
