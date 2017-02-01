package at.fhjoanneum.ippr.pmstorage.examples;

import at.fhjoanneum.ippr.commons.dto.owlimport.*;
import at.fhjoanneum.ippr.persistence.entities.model.businessobject.BusinessObjectModelBuilder;
import at.fhjoanneum.ippr.persistence.entities.model.businessobject.field.BusinessObjectFieldModelBuilder;
import at.fhjoanneum.ippr.persistence.entities.model.businessobject.permission.BusinessObjectFieldPermissionBuilder;
import at.fhjoanneum.ippr.persistence.entities.model.messageflow.MessageFlowBuilder;
import at.fhjoanneum.ippr.persistence.entities.model.process.ProcessModelBuilder;
import at.fhjoanneum.ippr.persistence.entities.model.state.StateBuilder;
import at.fhjoanneum.ippr.persistence.entities.model.subject.SubjectModelBuilder;
import at.fhjoanneum.ippr.persistence.entities.model.transition.TransitionBuilder;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.BusinessObjectModel;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.field.BusinessObjectFieldModel;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.permission.BusinessObjectFieldPermission;
import at.fhjoanneum.ippr.persistence.objects.model.enums.*;
import at.fhjoanneum.ippr.persistence.objects.model.messageflow.MessageFlow;
import at.fhjoanneum.ippr.persistence.objects.model.process.ProcessModel;
import at.fhjoanneum.ippr.persistence.objects.model.state.State;
import at.fhjoanneum.ippr.persistence.objects.model.subject.SubjectModel;
import at.fhjoanneum.ippr.persistence.objects.model.transition.Transition;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;


@Component
@Transactional
public class VacationRequestFromOWL extends AbstractExample {

  private static final Logger LOG = LoggerFactory.getLogger(VacationRequestFromOWL.class);
  private OWLProcessModelDTO processModelDTO;
  private List<OWLStateDTO> stateDTOs;
  private HashSet<OWLTransitionDTO> transitionDTOs;
  private HashSet<OWLBomDTO> bomDTOs;
  private List<OWLMessageFlowDTO> messageFlowDTOs;
  private Map<String, OWLStateDTO> stateDTOMap;
  private Map<String, OWLBomDTO> bomDTOMap;
  private Map<String, OWLSubjectModelDTO> subjectModelDTOMap;

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  protected EntityManager getEntityManager() {
    return entityManager;
  }

  public OWLProcessModelDTO getProcessModelDTO(){
      this.createData();
      return this.processModelDTO;
  }

  @Override
  protected void createData() {
      String URI_STANDARD = "http://www.imi.kit.edu/standard-pass-ont#";
      String URI_PROCESS_MODEL = URI_STANDARD+"PASSProcessModel";
      String URI_NAME = URI_STANDARD+"hasModelComponentLable";
      String URI_IDENTIFIER = URI_STANDARD+"hasModelComponentID";
      String URI_ACTOR = URI_STANDARD+"SingleActor";
      String URI_BEHAVIOR = URI_STANDARD+"hasBehavior";
      String URI_STATE = URI_STANDARD+"hasState";
      String URI_FUNCTION_STATE = URI_STANDARD+"FunctionState";
      String URI_SEND_STATE = URI_STANDARD+"SendState";
      String URI_RECEIVE_STATE = URI_STANDARD+"ReceiveState";
      String URI_INITIAL_STATE = URI_STANDARD+"InitialState";
      String URI_END_STATE = URI_STANDARD+"EndState";
      String URI_HAS_EDGE = URI_STANDARD+"hasEdge";
      String URI_RECEIVE_TRANSITION = URI_STANDARD+"ReceiveTransition";
      String URI_STANDARD_TRANSITION = URI_STANDARD+"StandardTransition";
      String URI_SEND_TRANSITION = URI_STANDARD+"SendTransition";
      String URI_SOURCE_STATE = URI_STANDARD+"hasSourceState";
      String URI_TARGET_STATE = URI_STANDARD+"hasTargetState";
      String URI_REFERS_TO = URI_STANDARD+"refersTo";
      String URI_SENDER = URI_STANDARD+"hasSender";
      String URI_RECEIVER = URI_STANDARD+"hasReceiver";
      String URI_MESSAGE_TYPE = URI_STANDARD+"hasMessageType";

        stateDTOs = new ArrayList<>();
        transitionDTOs = new HashSet<>();
        bomDTOs = new HashSet<>();
        messageFlowDTOs = new ArrayList<>();
        stateDTOMap = new HashMap<>();
        bomDTOMap = new HashMap<>();
        subjectModelDTOMap = new HashMap<>();


	  try {
		InputStream is = this.getClass().getResourceAsStream("/ontologies/VacationRequest.owl");

		OntModel model = ModelFactory.createOntologyModel();
        OntDocumentManager dm  = model.getDocumentManager();
		model.read(is, "OWL/XML");
		model.write(System.out);
		//Find Process Model
		List<? extends OntResource> processModels = model.getOntClass(URI_PROCESS_MODEL).listInstances().toList();
		for(OntResource processModel : processModels) {
            Property labelProperty = model.getProperty(URI_NAME);
            Property identifierProperty = model.getProperty(URI_IDENTIFIER);
            Property behaviorProperty = model.getProperty(URI_BEHAVIOR);
            Property stateProperty = model.getProperty(URI_STATE);
            Property functionStateProperty = model.getProperty(URI_FUNCTION_STATE);
            Property sendStateProperty = model.getProperty(URI_SEND_STATE);
            Property receiveStateProperty = model.getProperty(URI_RECEIVE_STATE);
            Property initialStateProperty = model.getProperty(URI_INITIAL_STATE);
            Property endStateProperty = model.getProperty(URI_END_STATE);
            Property typeProperty = model.getProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
            Property transitionProperty = model.getProperty(URI_HAS_EDGE);
            Property receiveTransitionProperty = model.getProperty(URI_RECEIVE_TRANSITION);
            Property standardTransitionProperty = model.getProperty(URI_STANDARD_TRANSITION);
            Property sendTransitionProperty = model.getProperty(URI_SEND_TRANSITION);
            Property sourceStateProperty = model.getProperty(URI_SOURCE_STATE);
            Property targetStateProperty = model.getProperty(URI_TARGET_STATE);
            Property refersToProperty = model.getProperty(URI_REFERS_TO);
            Property senderProperty = model.getProperty(URI_SENDER);
            Property receiverProperty = model.getProperty(URI_RECEIVER);
            Property messageTypeProperty = model.getProperty(URI_MESSAGE_TYPE);

            //Get ProcessModelName
            String processName = processModel.getProperty(labelProperty).getString();
            System.out.println("Process Name "+processName);


            //Find Actors in ProcessModel
            List<? extends OntResource> actors = model.getOntClass(URI_ACTOR).listInstances().toList();
            List actorNames = new ArrayList();
            for(OntResource actor : actors) {
                //Get ActorName
                String actorName = actor.getProperty(labelProperty).getString();
                actorNames.add(actorName);
                System.out.println("Found Actor: "+actorName);

                String subjectModelIdentifier = actor.getProperty(identifierProperty).getString();
                OWLSubjectModelDTO subjectModelDTO = subjectModelDTOMap.get(subjectModelIdentifier);
                if(subjectModelDTO == null){
                    subjectModelDTO = new OWLSubjectModelDTO(actorName);
                }
                subjectModelDTOMap.put(subjectModelIdentifier, subjectModelDTO);

                //Find behaviour for actor
                Resource behavior = actor.getProperty(behaviorProperty).getResource();

                //Find states
                List<org.apache.jena.rdf.model.Statement> states = behavior.listProperties(stateProperty).toList();
                for(Statement state : states) {
                    Resource stateResource = state.getResource();
                    String stateLabel = stateResource.getProperty(labelProperty).getString();
                    System.out.println("-With State: "+stateLabel);

                    String stateIdentifier = stateResource.getProperty(identifierProperty).getString();

                    String stateFunctionType = StateFunctionType.FUNCTION.toString();
                    String stateEventType = "";

                    // Which type of state?
                    if(stateResource.hasProperty(typeProperty,endStateProperty)){
                        System.out.println("--is EndState");
                        stateEventType = StateEventType.END.toString();
                    } else if (stateResource.hasProperty(typeProperty,initialStateProperty)){
                        System.out.println("--is InitialState");
                        stateEventType = StateEventType.START.toString();
                    }
                    if(stateResource.hasProperty(typeProperty,functionStateProperty)){
                        System.out.println("--is FunctionState");
                        stateFunctionType = StateFunctionType.FUNCTION.toString();
                    } else if(stateResource.hasProperty(typeProperty,sendStateProperty)){
                        System.out.println("--is SendState");
                        stateFunctionType = StateFunctionType.SEND.toString();
                    } else if(stateResource.hasProperty(typeProperty,receiveStateProperty)){
                        System.out.println("--is ReceiveState");
                        stateFunctionType = StateFunctionType.RECEIVE.toString();
                    }

                    OWLStateDTO stateDTO = new OWLStateDTO(stateLabel, subjectModelDTO, stateFunctionType, stateEventType);
                    stateDTOMap.put(stateIdentifier, stateDTO);
                    stateDTOs.add(stateDTO);
                }


                //Find Transitions (edges)
                List<org.apache.jena.rdf.model.Statement> transitions = behavior.listProperties(transitionProperty).toList();
                for(Statement transition : transitions) {
                    Resource transitionResource = transition.getResource();
                    String transitionLabel = transitionResource.getProperty(labelProperty).getString();
                    System.out.println("-With Transition: "+transitionLabel);

                    Resource sourceState = transitionResource.getProperty(sourceStateProperty).getResource();
                    System.out.println("--Has Source State: "+sourceState.getProperty(labelProperty).getString());
                    String sourceStateIdentifier = sourceState.getProperty(identifierProperty).getString();

                    Resource targetState = transitionResource.getProperty(targetStateProperty).getResource();
                    System.out.println("--Has Target State: "+targetState.getProperty(labelProperty).getString());
                    String targetStateIdentifier = targetState.getProperty(identifierProperty).getString();


                    OWLTransitionDTO transitionDTO = new OWLTransitionDTO(stateDTOMap.get(sourceStateIdentifier), stateDTOMap.get(targetStateIdentifier));
                    transitionDTOs.add(transitionDTO);

                    //Which type of transition?
                    if(transitionResource.hasProperty(typeProperty,standardTransitionProperty)){
                        System.out.println("--is StandardTransition");
                    } else if(transitionResource.hasProperty(typeProperty,receiveTransitionProperty)){
                        System.out.println("--is ReceiveTransition");
                    } else if(transitionResource.hasProperty(typeProperty,sendTransitionProperty)){
                        System.out.println("--is SendTransition");
                    }

                    if(transitionResource.hasProperty(refersToProperty)){
                        Resource refersTo = transitionResource.getProperty(refersToProperty).getResource();
                        Resource sender = refersTo.getProperty(senderProperty).getResource();
                        Resource receiver = refersTo.getProperty(receiverProperty).getResource();
                        String messageFlowLabel = refersTo.getProperty(labelProperty).getString();
                        System.out.println("--With MessageFlow "+messageFlowLabel);
                        System.out.println("---With Sender "+sender.getProperty(labelProperty).getString());
                        System.out.println("---With Receiver "+receiver.getProperty(labelProperty).getString());

                        Resource messageType = refersTo.getProperty(messageTypeProperty).getResource();
                        String messageTypeLabel = messageType.getProperty(labelProperty).getString();
                        System.out.println("---With Message "+messageTypeLabel);
                        String bomIdentifier = messageType.getProperty(identifierProperty).getString();

                        List<OWLStateDTO> transitionStateDTOs = new ArrayList<>();
                        transitionStateDTOs.add(stateDTOMap.get(sourceStateIdentifier));
                        transitionStateDTOs.add(stateDTOMap.get(targetStateIdentifier));
                        OWLBomDTO bomDTO = bomDTOMap.get(bomIdentifier);
                        if(bomDTO == null){
                            bomDTO = new OWLBomDTO(messageTypeLabel, transitionStateDTOs);
                            bomDTOMap.put(bomIdentifier, bomDTO);
                        } else {
                            bomDTO.getStates().addAll(transitionStateDTOs);
                        }
                        bomDTOs.add(bomDTO);

                        String senderIdentifier = sender.getProperty(identifierProperty).getString();
                        String receiverIdentifier = receiver.getProperty(identifierProperty).getString();
                        OWLSubjectModelDTO senderDTO = subjectModelDTOMap.get(senderIdentifier);
                        OWLSubjectModelDTO receiverDTO = subjectModelDTOMap.get(receiverIdentifier);
                        if(senderDTO == null){
                            senderDTO = new OWLSubjectModelDTO(sender.getProperty(labelProperty).getString());
                            subjectModelDTOMap.put(senderIdentifier, senderDTO);
                        }
                        if(receiverDTO == null){
                            receiverDTO = new OWLSubjectModelDTO(receiver.getProperty(labelProperty).getString());
                            subjectModelDTOMap.put(receiverIdentifier, receiverDTO);
                        }

                        //null sollte der State sein in unserem Fall, aber im OWL ist das auf der transition und nicht im state??
                        //OWLMessageFlowDTO messageFlowDTO = new OWLMessageFlowDTO(senderDTO, receiverDTO, null, bomDTO);
                    }



                }

                System.out.println("End");

                /*for(transition:transitions){
                    resource = transition.getResource()
                    resource.getProperty(labelProperty).getString();
                    if resource.hasProperty(type,standardTransitionProperty)
                    else if resource.hasProperty(type,receiveTransitionProperty)
                    else if resource.hasProperty(type,sendTransitionProperty)

                    sourceState = resource.getProperty(hasSourceStateProperty)
                    targetState = resource.getProperty(hasTargetStateProperty)
                    refersTo = resource.getProperty(refersToProperty)
                    if(refersTo)
                        messageFlow = refersTo.getResource();
                        messageFlow.getProperty(labelProperty).getString();
                        sender = messageFlow.getProperty(hasSender);
                        receiver = messageFlow.getProperty(hasReceiver); //-> ist nur die Resource. Alle Objekte sollten irgendwie mit Ids behandelt werden.
                        messageType = messageFlow.getProperty(hasMessageType); //-> ist nur die Resource. Alle Objekte sollten irgendwie mit Ids behandelt werden.
                        message = messageType.getResource();
                        messageLabel = message.getProperty(labelProperty).getString();


                }
                 */

            }

            processModelDTO = new OWLProcessModelDTO(processName, LocalDateTime.now(), new ArrayList<>(subjectModelDTOMap.values()), stateDTOs, new ArrayList<>(transitionDTOs), new ArrayList<>(bomDTOs), null);
        }
      } catch (Exception e) {
          e.printStackTrace();
      }
  }

  private SubjectModel createSubjectModel(String name, String assignedGroup, List<String> assignedRules){
      SubjectModelBuilder builder = new SubjectModelBuilder();
      builder.name(name).assignedGroup(assignedGroup);
      for(String rule:assignedRules){
          builder.addAssignedRule(rule);
      }
      return builder.build();
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
