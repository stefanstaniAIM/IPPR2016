package at.fhjoanneum.ippr.persistence.objects.engine.state;

import at.fhjoanneum.ippr.persistence.objects.engine.process.ProcessInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.subject.Subject;
import at.fhjoanneum.ippr.persistence.objects.model.state.State;

public interface SubjectState {

  Long getSsId();

  State getCurrentState();

  ProcessInstance getProcessInstance();

  Subject getSubject();
}
