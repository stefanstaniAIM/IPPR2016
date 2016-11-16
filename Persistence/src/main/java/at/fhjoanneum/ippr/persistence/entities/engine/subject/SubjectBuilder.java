package at.fhjoanneum.ippr.persistence.entities.engine.subject;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import at.fhjoanneum.ippr.persistence.builder.Builder;
import at.fhjoanneum.ippr.persistence.entities.model.subject.SubjectModelImpl;
import at.fhjoanneum.ippr.persistence.objects.engine.subject.Subject;
import at.fhjoanneum.ippr.persistence.objects.model.subject.SubjectModel;

public class SubjectBuilder implements Builder<Subject> {

  private SubjectModelImpl subjectModel;
  private Long userId;
  private Long groupId;

  SubjectBuilder subjectModel(final SubjectModel subjectModel) {
    checkNotNull(subjectModel);
    checkArgument(subjectModel instanceof SubjectModelImpl);
    this.subjectModel = (SubjectModelImpl) subjectModel;
    return this;
  }

  SubjectBuilder userId(final Long userId) {
    checkNotNull(userId);
    this.userId = userId;
    return this;
  }

  SubjectBuilder groupId(final Long groupId) {
    checkNotNull(groupId);
    this.groupId = groupId;
    return this;
  }

  @Override
  public Subject build() {
    checkNotNull(subjectModel);
    checkArgument(groupId != null || userId != null);
    return new SubjectImpl(userId, groupId, subjectModel);
  }

}
