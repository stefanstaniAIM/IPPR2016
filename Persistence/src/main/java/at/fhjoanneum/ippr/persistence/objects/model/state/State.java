package at.fhjoanneum.ippr.persistence.objects.model.state;

import java.util.List;
import java.util.Optional;

import at.fhjoanneum.ippr.persistence.objects.model.businessobject.BusinessObjectModel;
import at.fhjoanneum.ippr.persistence.objects.model.enums.StateEventType;
import at.fhjoanneum.ippr.persistence.objects.model.enums.StateFunctionType;
import at.fhjoanneum.ippr.persistence.objects.model.messageflow.MessageFlow;
import at.fhjoanneum.ippr.persistence.objects.model.subject.SubjectModel;
import at.fhjoanneum.ippr.persistence.objects.model.transition.Transition;

public interface State {

  Long getSId();

  String getName();

  SubjectModel getSubjectModel();

  StateFunctionType getFunctionType();

  StateEventType getEventType();

  List<Transition> getFromStates();

  List<Transition> getToStates();

  Optional<Transition> getTimeoutTransition();

  List<MessageFlow> getMessageFlow();

  List<BusinessObjectModel> getBusinessObjectModels();

  Optional<MessageFlow> getMessageFlowForReceiver(final SubjectModel receiver);

  RefinementState getRefinementState();
}
