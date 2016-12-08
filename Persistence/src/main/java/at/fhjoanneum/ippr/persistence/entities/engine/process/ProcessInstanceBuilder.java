package at.fhjoanneum.ippr.persistence.entities.engine.process;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import com.google.common.collect.Lists;

import at.fhjoanneum.ippr.persistence.builder.Builder;
import at.fhjoanneum.ippr.persistence.entities.engine.subject.SubjectImpl;
import at.fhjoanneum.ippr.persistence.entities.model.process.ProcessModelImpl;
import at.fhjoanneum.ippr.persistence.objects.engine.process.ProcessInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.subject.Subject;
import at.fhjoanneum.ippr.persistence.objects.model.process.ProcessModel;

public class ProcessInstanceBuilder implements Builder<ProcessInstance> {

  private ProcessModelImpl processModel;
  private final List<SubjectImpl> subjects = Lists.newArrayList();
  private Long startUserId;


  public ProcessInstanceBuilder processModel(final ProcessModel processModel) {
    checkNotNull(processModel);
    checkArgument(processModel instanceof ProcessModelImpl);
    this.processModel = (ProcessModelImpl) processModel;
    return this;
  }

  public ProcessInstanceBuilder addSubject(final Subject subject) {
    checkNotNull(subject);
    checkArgument(subject instanceof SubjectImpl);
    this.subjects.add((SubjectImpl) subject);
    return this;
  }

  public ProcessInstanceBuilder startUserId(final Long startUserId) {
    checkNotNull(startUserId);
    this.startUserId = startUserId;
    return this;
  }

  @Override
  public ProcessInstance build() {
    checkNotNull(processModel);
    checkArgument(!subjects.isEmpty());
    return new ProcessInstanceImpl(processModel, subjects, startUserId);
  }
}
