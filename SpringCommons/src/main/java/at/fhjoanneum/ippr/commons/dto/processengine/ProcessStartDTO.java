package at.fhjoanneum.ippr.commons.dto.processengine;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProcessStartDTO implements Serializable {

  private static final long serialVersionUID = 7754916772226745990L;

  private Long pmId;
  private Long startUserId;

  public ProcessStartDTO() {}

  public Long getPmId() {
    return pmId;
  }

  public Long getStartUserId() {
    return startUserId;
  }
}
