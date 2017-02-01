package at.fhjoanneum.ippr.commons.dto.owlimport;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.time.LocalDateTime;


@XmlRootElement
public class OWLProcessModelDTO implements Serializable {

  private Long pmId;
  private String name;
  private String description;
  private LocalDateTime createdAt;

  public OWLProcessModelDTO() {}

  public OWLProcessModelDTO(final Long pmId, final String name, final String description,
                            final LocalDateTime createdAt) {
    this.pmId = pmId;
    this.name = name;
    this.description = description;
    this.createdAt = createdAt;
  }

  public Long getPmId() {
    return pmId;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }
}
