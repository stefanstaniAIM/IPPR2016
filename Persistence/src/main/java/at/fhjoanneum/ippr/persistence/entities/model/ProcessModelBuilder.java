package at.fhjoanneum.ippr.persistence.entities.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.fhjoanneum.ippr.persistence.builder.Builder;
import at.fhjoanneum.ippr.persistence.builder.BuilderUtils;
import at.fhjoanneum.ippr.persistence.objects.model.ProcessModel;
import at.fhjoanneum.ippr.persistence.objects.model.enums.ProcessModelState;

public class ProcessModelBuilder implements Builder<ProcessModel> {

	private static final Logger LOG = LogManager.getLogger(ProcessModelBuilder.class);

	private String name;
	private String description;
	private ProcessModelState state;

	public ProcessModelBuilder name(final String name) {
		BuilderUtils.isNotBlank(name);
		this.name = name;
		return this;
	}

	public ProcessModelBuilder description(final String description) {
		BuilderUtils.isNotBlank(description);
		this.description = description;
		return this;
	}

	public ProcessModelBuilder state(final ProcessModelState state) {
		BuilderUtils.isNotNull(state);
		this.state = state;
		return this;
	}

	@Override
	public ProcessModel build() {
		BuilderUtils.isNotBlank(name);
		BuilderUtils.isNotBlank(description);
		BuilderUtils.isNotNull(state);

		final ProcessModelmpl processModel = new ProcessModelmpl(name, description, state);
		return processModel;
	}
}
