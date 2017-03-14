package at.fhjoanneum.ippr.persistence.entities.model.subject;

import static at.fhjoanneum.ippr.persistence.builder.BuilderUtils.isNotBlank;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import at.fhjoanneum.ippr.persistence.builder.Builder;
import at.fhjoanneum.ippr.persistence.objects.model.enums.SubjectModelType;
import at.fhjoanneum.ippr.persistence.objects.model.subject.SubjectModel;

public class SubjectModelBuilder implements Builder<SubjectModel> {

  private String name;
  private final List<String> assignedRules = Lists.newArrayList();
  private SubjectModelType type;

  public SubjectModelBuilder name(final String name) {
    isNotBlank(name);
    this.name = name;
    return this;
  }

  public SubjectModelBuilder type(final SubjectModelType type) {
    Preconditions.checkNotNull(type);
    this.type = type;
    return this;
  }

  public SubjectModelBuilder addAssignedRule(final String rule) {
    isNotBlank(rule);
    assignedRules.add(rule);
    return this;
  }

  @Override
  public SubjectModel build() {
    isNotBlank(name);
    if (type == null) {
      type = SubjectModelType.INTERNAL;
    }

    if (SubjectModelType.INTERNAL.equals(type)) {
      Preconditions.checkArgument(!assignedRules.isEmpty());
    }

    final SubjectModel subjectModel = new SubjectModelImpl(name, assignedRules, type);
    return subjectModel;
  }

}
