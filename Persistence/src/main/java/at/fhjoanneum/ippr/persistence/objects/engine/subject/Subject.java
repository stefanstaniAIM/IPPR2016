package at.fhjoanneum.ippr.persistence.objects.engine.subject;

import at.fhjoanneum.ippr.persistence.objects.engine.state.SubjectState;
import at.fhjoanneum.ippr.persistence.objects.model.subject.SubjectModel;

public interface Subject {

  Long getSId();

  SubjectModel getSubjectModel();

  Long getUser();

  Long getGroup();

  SubjectState getSubjectState();
}
