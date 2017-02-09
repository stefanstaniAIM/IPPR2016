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
  private String state;
  private Float version;

  public ProcessModelDTO() {}

  public ProcessModelDTO(final Long pmId, final String name, final String description,
      final LocalDateTime createdAt, final List<SubjectModelDTO> subjectModels) {
    this.pmId = pmId;
    this.name = name;
    this.description = description;
    this.createdAt = createdAt;
    this.subjectModels = subjectModels;
  }

  public ProcessModelDTO(final Long pmId, final String name, final String description,
      final LocalDateTime createdAt, final List<SubjectModelDTO> subjectModels, final String state,
      final Float version) {
    this(pmId, name, description, createdAt, subjectModels);
    this.state = state;
    this.version = version;
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

  public String getState() {
    return state;
  }

  public Float getVersion() {
    return version;
  }
}
