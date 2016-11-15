package at.fhjoanneum.ippr.persistence.entities.engine.state;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import at.fhjoanneum.ippr.persistence.entities.engine.process.ProcessInstanceImpl;
import at.fhjoanneum.ippr.persistence.entities.engine.subject.SubjectImpl;
import at.fhjoanneum.ippr.persistence.entities.model.state.StateImpl;
import at.fhjoanneum.ippr.persistence.objects.engine.state.SubjectState;
import at.fhjoanneum.ippr.persistence.objects.model.state.State;

@Entity(name = "SUBJECT_STATE")
public class SubjectStateImpl implements SubjectState, Serializable {

  private static final long serialVersionUID = -8145174613972009727L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long ssId;

  @ManyToOne
  @JoinColumn(name = "current_state")
  private StateImpl currentState;

  @ManyToOne
  @JoinColumn(name = "piId")
  private ProcessInstanceImpl processInstance;

  @OneToOne
  @JoinColumn(name = "s_id")
  private SubjectImpl subject;

  @Override
  public State getCurrentState() {
    return currentState;
  }

  public void setCurrentState(final State currentState) {
    this.currentState = (StateImpl) currentState;
  }

  @Override
  public Long getSsId() {
    return ssId;
  }

  @Override
  public ProcessInstanceImpl getProcessInstance() {
    return processInstance;
  }

  @Override
  public SubjectImpl getSubject() {
    return subject;
  }

  @Override
  public int hashCode() {
    return Objects.hash(ssId);
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final SubjectStateImpl other = (SubjectStateImpl) obj;
    if (ssId == null) {
      if (other.ssId != null)
        return false;
    } else if (!ssId.equals(other.ssId))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("ssId", ssId)
        .append("subject", subject).append("currentState", currentState.getName()).toString();
  }
}
