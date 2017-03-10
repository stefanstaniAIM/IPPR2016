package at.fhjoanneum.ippr.persistence.entities.model.subject;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import at.fhjoanneum.ippr.persistence.entities.model.process.ProcessModelImpl;
import at.fhjoanneum.ippr.persistence.entities.model.state.StateImpl;
import at.fhjoanneum.ippr.persistence.objects.model.enums.SubjectModelType;
import at.fhjoanneum.ippr.persistence.objects.model.process.ProcessModel;
import at.fhjoanneum.ippr.persistence.objects.model.state.State;
import at.fhjoanneum.ippr.persistence.objects.model.subject.SubjectModel;

@Entity(name = "SUBJECT_MODEL")
public class SubjectModelImpl implements SubjectModel, Serializable {

  private static final long serialVersionUID = 6629392913045430776L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long smId;

  @Column
  @NotBlank
  @Size(min = 1, max = 100)
  private String name;

  @Column(name = "subject_model_type")
  @NotNull
  @Enumerated(EnumType.STRING)
  private SubjectModelType type;

  @ManyToOne
  @JoinColumn(name = "pm_id")
  private ProcessModelImpl processModel;

  @OneToMany(mappedBy = "subjectModel")
  @NotNull
  private final List<StateImpl> states = Lists.newArrayList();

  @ElementCollection
  @CollectionTable(name = "SUBJECT_MODEL_RULE", joinColumns = @JoinColumn(name = "sm_id"))
  @Column(name = "name")
  private List<String> assignedRules = Lists.newArrayList();

  SubjectModelImpl() {}

  SubjectModelImpl(final String name, final List<String> assignedRules,
      final SubjectModelType type) {
    this.name = name;
    this.assignedRules = assignedRules;
    this.type = type;
  }

  @Override
  public Long getSmId() {
    return smId;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public SubjectModelType getSubjectModelType() {
    return type;
  }

  @Override
  public List<State> getStates() {
    return ImmutableList.copyOf(states);
  }

  @Override
  public List<String> getAssignedRules() {
    return ImmutableList.copyOf(assignedRules);
  }

  @Override
  public ProcessModel getProcessModel() {
    return processModel;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null) {
      return false;
    }
    if (!SubjectModel.class.isAssignableFrom(obj.getClass())) {
      return false;
    }
    final SubjectModel other = (SubjectModel) obj;
    if ((this.smId == null) ? (other.getSmId() != null) : !this.smId.equals(other.getSmId())) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(smId);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("smId", smId)
        .append("name", name).toString();
  }
}
