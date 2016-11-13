package at.fhjoanneum.ippr.commons.dto.pmstorage;

import java.time.LocalDateTime;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class ProcessModelDTO {

  private Long pmId;
  private String name;
  private String desription;
  private LocalDateTime createdAt;
  private List<SubjectModelDTO> subjectModels;

  public ProcessModelDTO() {}

  public ProcessModelDTO(final Long pmId, final String name, final String desription,
      final LocalDateTime createdAt, final List<SubjectModelDTO> subjectModels) {
    this.pmId = pmId;
    this.name = name;
    this.desription = desription;
    this.createdAt = createdAt;
    this.subjectModels = subjectModels;
  }

  public Long getPmId() {
    return pmId;
  }

  public String getName() {
    return name;
  }

  public String getDesription() {
    return desription;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public List<SubjectModelDTO> getSubjectModels() {
    return subjectModels;
  }
}
