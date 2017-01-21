package at.fhjoanneum.ippr.pmstorage.examples;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import at.fhjoanneum.ippr.persistence.entities.model.businessobject.BusinessObjectModelBuilder;
import at.fhjoanneum.ippr.persistence.entities.model.businessobject.field.BusinessObjectFieldModelBuilder;
import at.fhjoanneum.ippr.persistence.entities.model.businessobject.permission.BusinessObjectFieldPermissionBuilder;
import at.fhjoanneum.ippr.persistence.entities.model.messageflow.MessageFlowBuilder;
import at.fhjoanneum.ippr.persistence.entities.model.process.ProcessModelBuilder;
import at.fhjoanneum.ippr.persistence.entities.model.state.StateBuilder;
import at.fhjoanneum.ippr.persistence.entities.model.subject.SubjectModelBuilder;
import at.fhjoanneum.ippr.persistence.entities.model.transition.TransitionBuilder;
import at.fhjoanneum.ippr.persistence.objects.engine.subject.Subject;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.BusinessObjectModel;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.field.BusinessObjectFieldModel;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.permission.BusinessObjectFieldPermission;
import at.fhjoanneum.ippr.persistence.objects.model.enums.*;
import at.fhjoanneum.ippr.persistence.objects.model.messageflow.MessageFlow;
import at.fhjoanneum.ippr.persistence.objects.model.process.ProcessModel;
import at.fhjoanneum.ippr.persistence.objects.model.state.State;
import at.fhjoanneum.ippr.persistence.objects.model.subject.SubjectModel;
import at.fhjoanneum.ippr.persistence.objects.model.transition.Transition;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javassist.bytecode.Descriptor.Iterator;



@Component
@Transactional
public class VacationRequestFromOWL extends AbstractExample {

  private static final Logger LOG = LoggerFactory.getLogger(VacationRequestFromOWL.class);

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  protected EntityManager getEntityManager() {
    return entityManager;
  }

  @Override
  protected void createData() {
      String URI_STANDARD = "http://www.imi.kit.edu/standard-pass-ont#";
      String URI_PROCESS_MODEL = URI_STANDARD+"PASSProcessModel";
      String URI_NAME = URI_STANDARD+"hasModelComponentLable";
      String URI_ACTOR = URI_STANDARD+"SingleActor";
      String URI_BEHAVIOR = URI_STANDARD+"hasBehavior";
      String URI_STATE = URI_STANDARD+"hasState";
      String URI_FUNCTION_STATE = URI_STANDARD+"FunctionState";
      String URI_SEND_STATE = URI_STANDARD+"SendState";
      String URI_RECEIVE_STATE = URI_STANDARD+"ReceiveState";
      String URI_START_STATE = URI_STANDARD+"StartState";
      String URI_END_STATE = URI_STANDARD+"EndState";

	  try {
		InputStream is = this.getClass().getResourceAsStream("/ontologies/VacationRequest.owl");

		OntModel model = ModelFactory.createOntologyModel();
        OntDocumentManager dm  = model.getDocumentManager();
		model.read(is, "OWL/XML");
		model.write(System.out);
		List<? extends OntResource> processModels = model.getOntClass(URI_PROCESS_MODEL).listInstances().toList();
		for(OntResource processModel : processModels) {
            Property labelProperty = model.getProperty(URI_NAME);
            Property behaviorProperty = model.getProperty(URI_BEHAVIOR);
            Property stateProperty = model.getProperty(URI_STATE);
            Property functionStateProperty = model.getProperty(URI_FUNCTION_STATE);
            Property sendStateProperty = model.getProperty(URI_SEND_STATE);
            Property receiveStateProperty = model.getProperty(URI_RECEIVE_STATE);
            Property startStateProperty = model.getProperty(URI_START_STATE);
            Property endStateProperty = model.getProperty(URI_END_STATE);

            String processName = processModel.getProperty(labelProperty).getString();
            System.out.println("Process Name "+processName);
            List<? extends OntResource> actors = model.getOntClass(URI_ACTOR).listInstances().toList();
            List actorNames = new ArrayList();
            for(OntResource actor : actors) {
                String actorName = actor.getProperty(labelProperty).getString();
                actorNames.add(actorName);
                System.out.println("Found Actor: "+actorName);
                Resource behaviorUri = actor.getPropertyResourceValue(behaviorProperty);
                OntResource behavior =  model.getOntResource(behaviorUri);
                List<org.apache.jena.rdf.model.RDFNode> states = behavior.listPropertyValues(stateProperty).toList();
                for(RDFNode state : states) {
                    Resource stateResource = state.asResource();
                    //model.getOntResource(stateResource);
                    String stateLabel = stateResource.getProperty(labelProperty).getString();
                    System.out.println("-With State: "+stateLabel);
                    // wie überprüfen ob function, receive, send, end, start ??
                    stateResource.hasProperty(receiveStateProperty);
                }
            }
		}
      } catch (Exception e) {
	  	e.printStackTrace();
	  }
   
  }

  private SubjectModel createSubjectModel(String name, String assignedGroup){
      return new SubjectModelBuilder().name(name).assignedGroup(assignedGroup).build();
  }

  private ProcessModel createProcessModel(String name, String description, List<SubjectModel> subjectModels, SubjectModel starterSubject) {
      ProcessModelBuilder builder = new ProcessModelBuilder();
      builder.name(name).description(description).state(ProcessModelState.ACTIVE);
      for(SubjectModel sm : subjectModels ){
          builder.addSubjectModel(sm);
      }
      builder.starterSubject(starterSubject);
      builder.version(1.1F);
      return builder.build();
  }

  private State createState(String name, SubjectModel subjectModel, StateFunctionType functionType, StateEventType eventType){
      StateBuilder builder = new StateBuilder();
      builder.subjectModel(subjectModel);
      builder.name(name);
      builder.functionType(functionType);
      if(eventType != null) {
          builder.eventType(eventType);
      }
      return builder.build();
  }

  private Transition createTransition(State fromState, State toState){
      TransitionBuilder builder = new TransitionBuilder();
      builder.fromState(fromState).toState(toState);
      return builder.build();
  }

  private BusinessObjectModel createBusinessObjectModel(String name, List<State> states, BusinessObjectModel parent){
      BusinessObjectModelBuilder builder = new BusinessObjectModelBuilder();
      builder.name("Vacation request form");
      for(State state : states){
          builder.addToState(state);
      }
      if(parent != null){
          builder.parent(parent);
      }
      return builder.build();
  }

  private BusinessObjectFieldModel createBusinessObjectFieldModel(String name, FieldType type, BusinessObjectModel businessObjectModel){
      BusinessObjectFieldModelBuilder builder = new BusinessObjectFieldModelBuilder();
      builder.fieldName(name);
      builder.fieldType(type);
      builder.businessObjectModel(businessObjectModel);
      return builder.build();
  }

  private BusinessObjectFieldPermission createBusinessObjectFieldPermission(BusinessObjectFieldModel fieldModel, State state, FieldPermission permission, boolean mandatory){
      BusinessObjectFieldPermissionBuilder builder = new BusinessObjectFieldPermissionBuilder();
      builder.businessObjectFieldModel(fieldModel);
      builder.state(state);
      builder.permission(permission);
      builder.mandatory(mandatory);
      return builder.build();
  }

  private MessageFlow createMessageFlow(SubjectModel sender, SubjectModel receiver, State state, BusinessObjectModel businessObjectModel){
      MessageFlowBuilder builder = new MessageFlowBuilder();
      builder.sender(sender);
      builder.receiver(receiver);
      builder.state(state);
      builder.assignBusinessObjectModel(businessObjectModel);
      return builder.build();
  }

  @Override
  protected String getName() {
    return "Vacation Request From OWL";
  }
}
