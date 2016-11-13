package at.fhjoanneum.ippr.pmstorage.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProcessModelDTO {

  private final Long pmId;
  private final String name;
  private final String desription;
  private final LocalDateTime createdAt;
  private final List<SubjectModelDTO> subjectModels;

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
