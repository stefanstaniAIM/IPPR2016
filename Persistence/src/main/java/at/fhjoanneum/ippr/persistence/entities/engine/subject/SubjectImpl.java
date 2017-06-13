package at.fhjoanneum.ippr.persistence.entities.engine.subject;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.base.Preconditions;

import at.fhjoanneum.ippr.persistence.entities.engine.state.SubjectStateImpl;
import at.fhjoanneum.ippr.persistence.entities.model.subject.SubjectModelImpl;
import at.fhjoanneum.ippr.persistence.objects.engine.state.SubjectState;
import at.fhjoanneum.ippr.persistence.objects.engine.subject.Subject;

@Entity(name = "SUBJECT")
public class SubjectImpl implements Subject, Serializable {

  private static final long serialVersionUID = 6695845587070926143L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long sId;

  @Column
  private Long userId;

  @Column
  private Long groupId;

  @ManyToOne
  @JoinColumn(name = "smId")
  @NotNull
  private SubjectModelImpl subjectModel;

  @OneToOne(mappedBy = "subject")
  private SubjectStateImpl subjectState;

  @ManyToOne
  private SubjectImpl processPartner;

  SubjectImpl() {}

  SubjectImpl(final Long userId, final Long groupId, final SubjectModelImpl subjectModel) {
    this.userId = userId;
    this.groupId = groupId;
    this.subjectModel = subjectModel;
  }

  @Override
  public Long getSId() {
    return sId;
  }

  @Override
  public Long getUser() {
    return userId;
  }

  @Override
  public void setUser(final Long userId) {
    Preconditions.checkNotNull(userId);
    this.userId = userId;
  }

  @Override
  public Long getGroup() {
    return groupId;
  }

  @Override
  public SubjectModelImpl getSubjectModel() {
    return subjectModel;
  }

  @Override
  public SubjectState getSubjectState() {
    return subjectState;
  }

  @Override
  public Optional<Subject> getProcessPartner() {
    return Optional.ofNullable(processPartner);
  }

  @Override
  public void setProcessPartner(final Subject subject) {
    this.processPartner = (SubjectImpl) subject;
  }

  @Override
  public int hashCode() {
    return Objects.hash(sId);
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final SubjectImpl other = (SubjectImpl) obj;
    if (sId == null) {
      if (other.sId != null) {
        return false;
      }
    } else if (!sId.equals(other.sId)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("sId", sId)
        .append("user", userId).append("group", groupId).toString();
  }
}
