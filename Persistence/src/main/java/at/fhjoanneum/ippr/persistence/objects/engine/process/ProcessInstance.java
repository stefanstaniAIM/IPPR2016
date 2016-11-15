package at.fhjoanneum.ippr.persistence.objects.engine.process;

import java.time.LocalDateTime;
import java.util.List;

import at.fhjoanneum.ippr.persistence.objects.engine.enums.ProcessInstanceState;
import at.fhjoanneum.ippr.persistence.objects.engine.subject.Subject;

public interface ProcessInstance {

  Long getPiId();

  ProcessInstanceState getState();

  LocalDateTime getStartTime();

  LocalDateTime getEndTime();

  List<Subject> getSubjects();
}
