package at.fhjoanneum.ippr.persistence.entities.engine.state;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import at.fhjoanneum.ippr.persistence.builder.Builder;
import at.fhjoanneum.ippr.persistence.entities.engine.process.ProcessInstanceImpl;
import at.fhjoanneum.ippr.persistence.entities.engine.subject.SubjectImpl;
import at.fhjoanneum.ippr.persistence.entities.model.state.StateImpl;
import at.fhjoanneum.ippr.persistence.objects.engine.process.ProcessInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.state.SubjectState;
import at.fhjoanneum.ippr.persistence.objects.engine.subject.Subject;
import at.fhjoanneum.ippr.persistence.objects.model.state.State;

public class SubjectStateBuilder implements Builder<SubjectState> {

  private StateImpl state;
  private ProcessInstanceImpl processInstance;
  private SubjectImpl subject;

  SubjectStateBuilder state(final State state) {
    checkNotNull(state);
    checkArgument(state instanceof StateImpl);
    this.state = (StateImpl) state;
    return this;
  }

  SubjectStateBuilder processInstance(final ProcessInstance processInstance) {
    checkNotNull(processInstance);
    checkArgument(processInstance instanceof ProcessInstanceImpl);
    this.processInstance = (ProcessInstanceImpl) processInstance;
    return this;
  }

  SubjectStateBuilder subject(final Subject subject) {
    checkNotNull(subject);
    checkArgument(subject instanceof SubjectImpl);
    this.subject = (SubjectImpl) subject;
    return this;
  }

  @Override
  public SubjectState build() {
    checkNotNull(state);
    checkNotNull(processInstance);
    checkNotNull(subject);

    return new SubjectStateImpl(state, processInstance, subject);
  }
}
