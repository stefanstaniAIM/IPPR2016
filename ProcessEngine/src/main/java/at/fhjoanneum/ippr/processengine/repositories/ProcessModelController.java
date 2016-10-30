package at.fhjoanneum.ippr.processengine.repositories;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import at.fhjoanneum.ippr.persistence.entities.model.ProcessModelBuilder;
import at.fhjoanneum.ippr.persistence.objects.model.ProcessModel;
import at.fhjoanneum.ippr.persistence.objects.model.enums.ProcessModelState;

@Controller
public class ProcessModelController {

	@Autowired
	private ProcessModelRepository pmr;

	@Transactional
	public void create() {
		final ProcessModel processModel = new ProcessModelBuilder().name("test").description("test description")
				.state(ProcessModelState.ACTIVE).build();

		pmr.save(processModel);
	}
}
