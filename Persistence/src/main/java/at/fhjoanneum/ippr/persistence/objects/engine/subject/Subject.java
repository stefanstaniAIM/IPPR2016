package at.fhjoanneum.ippr.persistence.objects.engine.subject;

import java.util.Optional;

import at.fhjoanneum.ippr.persistence.objects.engine.state.SubjectState;
import at.fhjoanneum.ippr.persistence.objects.model.subject.SubjectModel;

public interface Subject {

  Long getSId();

  SubjectModel getSubjectModel();

  void setUser(Long userId);

  Long getUser();

  Long getGroup();

  SubjectState getSubjectState();

  Optional<Subject> getProcessPartner();

  void setProcessPartner(Subject subject);
}
