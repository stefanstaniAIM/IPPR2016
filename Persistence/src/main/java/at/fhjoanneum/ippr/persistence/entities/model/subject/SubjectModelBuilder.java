package at.fhjoanneum.ippr.persistence.entities.model.subject;

import static at.fhjoanneum.ippr.persistence.builder.BuilderUtils.isNotBlank;

import at.fhjoanneum.ippr.persistence.builder.Builder;
import at.fhjoanneum.ippr.persistence.objects.model.subject.SubjectModel;

public class SubjectModelBuilder implements Builder<SubjectModel> {

	private String name;
	private String assignedGroup;

	public SubjectModelBuilder name(final String name) {
		isNotBlank(name);
		this.name = name;
		return this;
	}

	public SubjectModelBuilder assignedGroup(final String assignedGroup) {
		isNotBlank(assignedGroup);
		this.assignedGroup = assignedGroup;
		return this;
	}

	@Override
	public SubjectModel build() {
		isNotBlank(name);
		isNotBlank(assignedGroup);

		final SubjectModel subjectModel = new SubjectModelImpl(name, assignedGroup);
		return subjectModel;
	}

}
