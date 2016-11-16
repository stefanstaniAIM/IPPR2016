package at.fhjoanneum.ippr.commons.dto.pmstorage;

import java.time.LocalDateTime;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class ProcessModelDTO {

  private Long pmId;
  private String name;
  private String description;
  private LocalDateTime createdAt;
  private List<SubjectModelDTO> subjectModels;

  public ProcessModelDTO() {}

  public ProcessModelDTO(final Long pmId, final String name, final String description,
      final LocalDateTime createdAt, final List<SubjectModelDTO> subjectModels) {
    this.pmId = pmId;
    this.name = name;
    this.description = description;
    this.createdAt = createdAt;
    this.subjectModels = subjectModels;
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

  public List<SubjectModelDTO> getSubjectModels() {
    return subjectModels;
  }
}
