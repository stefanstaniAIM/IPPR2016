package at.fhjoanneum.ippr.persistence.objects.engine.process;

import java.time.LocalDateTime;
import java.util.List;

import at.fhjoanneum.ippr.persistence.objects.engine.enums.ProcessInstanceState;
import at.fhjoanneum.ippr.persistence.objects.engine.subject.Subject;

public interface ProcessInstance {

  Long getPiId();

  ProcessInstanceState getState();

  void setState(ProcessInstanceState state);

  LocalDateTime getStartTime();

  LocalDateTime getEndTime();

  void setEndTime();

  List<Subject> getSubjects();
}
