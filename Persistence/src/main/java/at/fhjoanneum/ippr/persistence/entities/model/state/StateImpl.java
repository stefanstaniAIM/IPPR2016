package at.fhjoanneum.ippr.persistence.entities.model.state;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

import at.fhjoanneum.ippr.persistence.entities.model.businessobject.BusinessObjectModelImpl;
import at.fhjoanneum.ippr.persistence.entities.model.messageflow.MessageFlowImpl;
import at.fhjoanneum.ippr.persistence.entities.model.subject.SubjectModelImpl;
import at.fhjoanneum.ippr.persistence.entities.model.transition.TransitionImpl;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.BusinessObjectModel;
import at.fhjoanneum.ippr.persistence.objects.model.enums.StateEventType;
import at.fhjoanneum.ippr.persistence.objects.model.enums.StateFunctionType;
import at.fhjoanneum.ippr.persistence.objects.model.messageflow.MessageFlow;
import at.fhjoanneum.ippr.persistence.objects.model.state.State;
import at.fhjoanneum.ippr.persistence.objects.model.subject.SubjectModel;
import at.fhjoanneum.ippr.persistence.objects.model.transition.Transition;

@Entity(name = "STATE")
public class StateImpl implements State, Serializable {

  private static final long serialVersionUID = 4443123919463434645L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long sId;

  @Override
  public Long getSId() {
    return sId;
  }

  @Column
  @NotBlank
  @Size(min = 1, max = 100)
  private String name;

  @Column
  @NotNull
  @Enumerated(EnumType.STRING)
  private StateFunctionType functionType;

  @Column
  @Enumerated(EnumType.STRING)
  private StateEventType eventType;

  @ManyToOne
  @JoinColumn(name = "sm_id")
  @NotNull
  private SubjectModelImpl subjectModel;

  @OneToMany(mappedBy = "fromState")
  private List<TransitionImpl> fromStates;

  @OneToMany(mappedBy = "toState")
  private List<TransitionImpl> toStates;

  @OneToMany(mappedBy = "state")
  private List<MessageFlowImpl> messageFlows;

  @ManyToMany(mappedBy = "states")
  private List<BusinessObjectModelImpl> businessObjectModels;

  StateImpl() {}

  StateImpl(final String name, final SubjectModelImpl subjectModel,
      final StateFunctionType functionType, final StateEventType eventType) {
    this.name = name;
    this.subjectModel = subjectModel;
    this.functionType = functionType;
    this.eventType = eventType;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public SubjectModel getSubjectModel() {
    return subjectModel;
  }

  @Override
  public StateFunctionType getFunctionType() {
    return functionType;
  }

  @Override
  public StateEventType getEventType() {
    return eventType;
  }

  @Override
  public List<Transition> getFromStates() {
    return ImmutableList.copyOf(fromStates);
  }

  @Override
  public List<Transition> getToStates() {
    return ImmutableList.copyOf(toStates);
  }

  @Override
  public List<MessageFlow> getMessageFlow() {
    return ImmutableList.copyOf(messageFlows);
  }

  @Override
  public List<BusinessObjectModel> getBusinessObjectModels() {
    return ImmutableList.copyOf(businessObjectModels);
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null) {
      return false;
    }
    if (!State.class.isAssignableFrom(obj.getClass())) {
      return false;
    }
    final State other = (State) obj;
    if ((this.sId == null) ? (other.getSId() != null) : !this.sId.equals(other.getSId())) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(sId);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("sId", sId)
        .append("name", name).append("functionType", functionType).append("eventType", eventType)
        .toString();
  }
}
