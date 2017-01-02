package at.fhjoanneum.ippr.persistence.entities.engine.process;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import at.fhjoanneum.ippr.persistence.entities.engine.businessobject.BusinessObjectInstanceImpl;
import at.fhjoanneum.ippr.persistence.entities.engine.subject.SubjectImpl;
import at.fhjoanneum.ippr.persistence.entities.model.process.ProcessModelImpl;
import at.fhjoanneum.ippr.persistence.objects.engine.businessobject.BusinessObjectInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.enums.ProcessInstanceState;
import at.fhjoanneum.ippr.persistence.objects.engine.process.ProcessInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.subject.Subject;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.BusinessObjectModel;
import at.fhjoanneum.ippr.persistence.objects.model.process.ProcessModel;

@Entity(name = "PROCESS_INSTANCE")
public class ProcessInstanceImpl implements ProcessInstance, Serializable {

  private static final long serialVersionUID = -4574968938479681668L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long piId;

  @ManyToOne
  @JoinColumn(name = "pmId")
  @NotNull
  private ProcessModelImpl processModel;

  @NotNull
  @Column
  private LocalDateTime startTime;

  @Column
  private LocalDateTime endTime;

  @NotNull
  @Column
  private Long startUserId;

  @Column
  @NotNull
  @Enumerated(EnumType.STRING)
  private ProcessInstanceState state;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "process_subject_instance_map", joinColumns = {@JoinColumn(name = "pi_id")},
      inverseJoinColumns = {@JoinColumn(name = "s_id")})
  private List<SubjectImpl> subjects = Lists.newArrayList();

  @OneToMany(mappedBy = "processInstance")
  private final List<BusinessObjectInstanceImpl> businessObjectInstances = Lists.newArrayList();

  ProcessInstanceImpl() {}

  ProcessInstanceImpl(final ProcessModelImpl processModel, final List<SubjectImpl> subjects,
      final Long startUserId) {
    this.processModel = processModel;
    this.subjects = subjects;
    this.startTime = LocalDateTime.now();
    this.state = ProcessInstanceState.ACTIVE;
    this.startUserId = startUserId;
  }

  @Override
  public Long getPiId() {
    return piId;
  }

  @Override
  public ProcessInstanceState getState() {
    return state;
  }

  @Override
  public ProcessModel getProcessModel() {
    return processModel;
  }

  @Override
  public Long getStartUserId() {
    return startUserId;
  }

  @Override
  public void setState(final ProcessInstanceState state) {
    checkNotNull(state);
    this.state = state;

    if (state.equals(ProcessInstanceState.CANCELLED_BY_SYSTEM)
        || state.equals(ProcessInstanceState.CANCELLED_BY_USER)
        || state.equals(ProcessInstanceState.FINISHED)) {
      endTime = LocalDateTime.now();
    }
  }

  @Override
  public LocalDateTime getStartTime() {
    return startTime;
  }

  @Override
  public LocalDateTime getEndTime() {
    return endTime;
  }

  @Override
  public void setEndTime() {
    this.endTime = LocalDateTime.now();
  }

  @Override
  public List<Subject> getSubjects() {
    return Lists.newArrayList(subjects);
  }

  @Override
  public List<BusinessObjectInstance> getBusinessObjectInstances() {
    return Lists.newArrayList(businessObjectInstances);
  }

  @Override
  public boolean isBusinessObjectInstanceOfModelCreated(
      final BusinessObjectModel businessObjectModel) {
    return businessObjectInstances.stream()
        .filter(boi -> businessObjectModel.equals(boi.getBusinessObjectModel())).count() >= 1;
  }

  @Override
  public boolean isStopped() {
    return ProcessInstanceState.FINISHED.equals(state)
        || ProcessInstanceState.CANCELLED_BY_SYSTEM.equals(state)
        || ProcessInstanceState.CANCELLED_BY_USER.equals(state);
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null) {
      return false;
    }
    if (!ProcessInstance.class.isAssignableFrom(obj.getClass())) {
      return false;
    }
    final ProcessInstance other = (ProcessInstance) obj;
    if ((this.piId == null) ? (other.getPiId() != null) : !this.piId.equals(other.getPiId())) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(piId);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("piId", piId)
        .append("state", state).toString();
  }
}
