package at.fhjoanneum.ippr.persistence.objects.model;

import java.util.List;

import at.fhjoanneum.ippr.persistence.objects.model.enums.StateEventType;
import at.fhjoanneum.ippr.persistence.objects.model.enums.StateFunctionType;

public interface State {

	Long getSId();

	String getName();

	SubjectModel getSubjectModel();

	StateFunctionType getFunctionType();

	StateEventType getEventType();

	List<Transition> getFromStates();

	List<Transition> getToStates();

	List<MessageFlow> getMessageFlow();

	List<BusinessObjectModel> getBusinessObjectModels();
}
