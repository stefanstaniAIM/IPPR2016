package at.fhjoanneum.ippr.commons.dto.processengine;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StateObjectDTO implements Serializable {

  private static final long serialVersionUID = -1073231546568305460L;

  private Long piId;
  private Long ssId;
  private List<BusinessObjectDTO> businessObjects;

  public StateObjectDTO() {}

  public StateObjectDTO(final Long piId, final Long ssId,
      final List<BusinessObjectDTO> businessObjects) {
    this.piId = piId;
    this.ssId = ssId;
    this.businessObjects = businessObjects;
  }

  public Long getPiId() {
    return piId;
  }

  public Long getSsId() {
    return ssId;
  }

  public List<BusinessObjectDTO> getBusinessObjects() {
    return businessObjects;
  }
}
