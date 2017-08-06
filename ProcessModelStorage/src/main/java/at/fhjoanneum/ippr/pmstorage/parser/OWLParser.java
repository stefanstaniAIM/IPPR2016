package at.fhjoanneum.ippr.pmstorage.parser;

import at.fhjoanneum.ippr.commons.dto.owlimport.reader.*;
import at.fhjoanneum.ippr.persistence.objects.model.enums.StateEventType;
import at.fhjoanneum.ippr.persistence.objects.model.enums.StateFunctionType;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OWLParser implements FileParser<String, OWLProcessModelDTO> {

  private OWLProcessModelDTO processModelDTO;
  private List<OWLStateDTO> stateDTOs;
  private HashSet<OWLTransitionDTO> transitionDTOs;
  private HashSet<OWLBomDTO> bomDTOs;
  private List<OWLMessageFlowDTO> messageFlowDTOs;
  private Map<String, OWLStateDTO> stateDTOMap;
  private Map<String, OWLBomDTO> bomDTOMap;
  private Map<String, OWLSubjectModelDTO> subjectModelDTOMap;

  @Override
  public OWLProcessModelDTO parseFile(final String input, final String version) {
    try {
      final String URI_STANDARD;
      final String URI_ACTOR;
      final String URI_BEHAVIOR;
      final String URI_STATE;
      final String URI_FUNCTION_STATE;
      final String URI_HAS_EDGE;

      if (version.equals("0.7.4")){
        URI_STANDARD = "http://www.i2pm.net/standard-pass-ont#";
        URI_ACTOR = URI_STANDARD + "FullySpecifiedSingleSubject";
        URI_BEHAVIOR = URI_STANDARD + "containsBehavior";
        URI_STATE = URI_STANDARD + "containsState";
        URI_FUNCTION_STATE = URI_STANDARD + "DoState";
        URI_HAS_EDGE = URI_STANDARD + "containsTransition";
      } else {
        URI_STANDARD = "http://www.imi.kit.edu/standard-pass-ont#";
        URI_ACTOR = URI_STANDARD + "SingleActor";
        URI_BEHAVIOR = URI_STANDARD + "hasBehavior";
        URI_STATE = URI_STANDARD + "hasState";
        URI_FUNCTION_STATE = URI_STANDARD + "FunctionState";
        URI_HAS_EDGE = URI_STANDARD + "hasEdge";
      }

      final String URI_PROCESS_MODEL = URI_STANDARD + "PASSProcessModel";
      final String URI_NAME = URI_STANDARD + "hasModelComponentLable";
      final String URI_IDENTIFIER = URI_STANDARD + "hasModelComponentID";
      final String URI_SEND_STATE = URI_STANDARD + "SendState";
      final String URI_RECEIVE_STATE = URI_STANDARD + "ReceiveState";
      final String URI_INITIAL_STATE = URI_STANDARD + "InitialState";
      final String URI_END_STATE = URI_STANDARD + "EndState";
      final String URI_SOURCE_STATE = URI_STANDARD + "hasSourceState";
      final String URI_TARGET_STATE = URI_STANDARD + "hasTargetState";
      final String URI_REFERS_TO = URI_STANDARD + "refersTo";
      final String URI_SENDER = URI_STANDARD + "hasSender";
      final String URI_RECEIVER = URI_STANDARD + "hasReceiver";
      final String URI_MESSAGE_TYPE = URI_STANDARD + "hasMessageType";

      stateDTOs = new ArrayList<>();
      transitionDTOs = new HashSet<>();
      bomDTOs = new HashSet<>();
      messageFlowDTOs = new ArrayList<>();
      stateDTOMap = new HashMap<>();
      bomDTOMap = new HashMap<>();
      subjectModelDTOMap = new HashMap<>();

      final InputStream is = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));

      final OntModel model = ModelFactory.createOntologyModel();
      final OntDocumentManager dm = model.getDocumentManager();
      model.read(is, "OWL/XML");
      model.write(System.out);
      // Find Process Model
      final List<? extends OntResource> processModels =
          model.getOntClass(URI_PROCESS_MODEL).listInstances().toList();
      for (final OntResource processModel : processModels) {
        final Property labelProperty = model.getProperty(URI_NAME);
        final Property identifierProperty = model.getProperty(URI_IDENTIFIER);
        final Property behaviorProperty = model.getProperty(URI_BEHAVIOR);
        final Property stateProperty = model.getProperty(URI_STATE);
        final Property functionStateProperty = model.getProperty(URI_FUNCTION_STATE);
        final Property sendStateProperty = model.getProperty(URI_SEND_STATE);
        final Property receiveStateProperty = model.getProperty(URI_RECEIVE_STATE);
        final Property initialStateProperty = model.getProperty(URI_INITIAL_STATE);
        final Property endStateProperty = model.getProperty(URI_END_STATE);
        final Property typeProperty =
            model.getProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
        final Property transitionProperty = model.getProperty(URI_HAS_EDGE);
        final Property sourceStateProperty = model.getProperty(URI_SOURCE_STATE);
        final Property targetStateProperty = model.getProperty(URI_TARGET_STATE);
        final Property refersToProperty = model.getProperty(URI_REFERS_TO);
        final Property senderProperty = model.getProperty(URI_SENDER);
        final Property receiverProperty = model.getProperty(URI_RECEIVER);
        final Property messageTypeProperty = model.getProperty(URI_MESSAGE_TYPE);

        // Get ProcessModelName
        final String processName = processModel.getProperty(labelProperty).getString();

        // Find Actors in ProcessModel
        final List<? extends OntResource> actors =
            model.getOntClass(URI_ACTOR).listInstances().toList();
        final List actorNames = new ArrayList();
        for (final OntResource actor : actors) {
          // Get ActorName
          final String actorName = actor.getProperty(labelProperty).getString();
          actorNames.add(actorName);

          final String subjectModelIdentifier = actor.getProperty(identifierProperty).getString();
          OWLSubjectModelDTO subjectModelDTO = subjectModelDTOMap.get(subjectModelIdentifier);
          if (subjectModelDTO == null) {
            subjectModelDTO = new OWLSubjectModelDTO(subjectModelIdentifier, actorName);
          }
          subjectModelDTOMap.put(subjectModelIdentifier, subjectModelDTO);

          // Find behaviour for actor
          final Resource behavior = actor.getProperty(behaviorProperty).getResource();

          // Find states
          final List<org.apache.jena.rdf.model.Statement> states =
              behavior.listProperties(stateProperty).toList();
          for (final Statement state : states) {
            final Resource stateResource = state.getResource();
            final String stateLabel = stateResource.getProperty(labelProperty).getString();

            final String stateIdentifier =
                stateResource.getProperty(identifierProperty).getString();

            String stateFunctionType = StateFunctionType.FUNCTION.toString();
            String stateEventType = "";

            // Which type of state?
            if (stateResource.hasProperty(typeProperty, endStateProperty)) {
              stateEventType = StateEventType.END.toString();
            } else if (stateResource.hasProperty(typeProperty, initialStateProperty)) {
              stateEventType = StateEventType.START.toString();
            }
            if (stateResource.hasProperty(typeProperty, functionStateProperty)) {
              stateFunctionType = StateFunctionType.FUNCTION.toString();
            } else if (stateResource.hasProperty(typeProperty, sendStateProperty)) {
              stateFunctionType = StateFunctionType.SEND.toString();
            } else if (stateResource.hasProperty(typeProperty, receiveStateProperty)) {
              stateFunctionType = StateFunctionType.RECEIVE.toString();
            }

            final OWLStateDTO stateDTO = new OWLStateDTO(stateIdentifier, stateLabel,
                subjectModelIdentifier, stateFunctionType, stateEventType);
            stateDTOMap.put(stateIdentifier, stateDTO);
            stateDTOs.add(stateDTO);
          }


          // Find Transitions (edges)
          final List<org.apache.jena.rdf.model.Statement> transitions =
              behavior.listProperties(transitionProperty).toList();
          for (final Statement transition : transitions) {
            final Resource transitionResource = transition.getResource();
            final String transitionLabel =
                transitionResource.getProperty(labelProperty).getString();

            final Resource sourceState =
                transitionResource.getProperty(sourceStateProperty).getResource();
            final String sourceStateIdentifier =
                sourceState.getProperty(identifierProperty).getString();

            final Resource targetState =
                transitionResource.getProperty(targetStateProperty).getResource();
            final String targetStateIdentifier =
                targetState.getProperty(identifierProperty).getString();


            final OWLTransitionDTO transitionDTO =
                new OWLTransitionDTO(sourceStateIdentifier, targetStateIdentifier);
            transitionDTOs.add(transitionDTO);

            // Which type of transition?
            if (transitionResource.hasProperty(refersToProperty)) {
              final Resource refersTo =
                  transitionResource.getProperty(refersToProperty).getResource();
              final String messageFlowIdentifier =
                  transitionResource.getProperty(identifierProperty).getString();
              final Resource sender = refersTo.getProperty(senderProperty).getResource();
              final Resource receiver = refersTo.getProperty(receiverProperty).getResource();
              final String messageFlowLabel = refersTo.getProperty(labelProperty).getString();

              final Resource messageType = refersTo.getProperty(messageTypeProperty).getResource();
              final String messageTypeLabel = messageType.getProperty(labelProperty).getString();
              final String bomIdentifier = messageType.getProperty(identifierProperty).getString();

              final List<String> transitionStateIds = new ArrayList<>();
              transitionStateIds.add(sourceStateIdentifier);
              transitionStateIds.add(targetStateIdentifier);
              OWLBomDTO bomDTO = bomDTOMap.get(bomIdentifier);
              if (bomDTO == null) {
                bomDTO = new OWLBomDTO(bomIdentifier, messageTypeLabel, transitionStateIds);
                bomDTOMap.put(bomIdentifier, bomDTO);
              } else {
                bomDTO.getStateIds().addAll(transitionStateIds);
              }
              bomDTOs.add(bomDTO);

              if (sourceState.hasProperty(typeProperty, sendStateProperty) || sourceState.hasProperty(typeProperty, receiveStateProperty)) {

                final String senderIdentifier = sender.getProperty(identifierProperty).getString();
                final String receiverIdentifier =
                    receiver.getProperty(identifierProperty).getString();
                OWLSubjectModelDTO senderDTO = subjectModelDTOMap.get(senderIdentifier);
                OWLSubjectModelDTO receiverDTO = subjectModelDTOMap.get(receiverIdentifier);
                if (senderDTO == null) {
                  senderDTO = new OWLSubjectModelDTO(senderIdentifier,
                      sender.getProperty(labelProperty).getString());
                  subjectModelDTOMap.put(senderIdentifier, senderDTO);
                }
                if (receiverDTO == null) {
                  receiverDTO = new OWLSubjectModelDTO(receiverIdentifier,
                      receiver.getProperty(labelProperty).getString());
                  subjectModelDTOMap.put(receiverIdentifier, receiverDTO);
                }

                // nur wenn source state = sendstate
                final OWLMessageFlowDTO messageFlowDTO = new OWLMessageFlowDTO(
                    messageFlowIdentifier, senderIdentifier, receiverIdentifier, bomIdentifier,
                    sourceStateIdentifier, targetStateIdentifier);
                messageFlowDTOs.add(messageFlowDTO);
              }
            }
          }
        }

        processModelDTO = new OWLProcessModelDTO(processName, LocalDateTime.now(),
            new ArrayList<>(subjectModelDTOMap.values()), stateDTOs,
            new ArrayList<>(transitionDTOs), new ArrayList<>(bomDTOs), messageFlowDTOs);

        return processModelDTO;
      }
    } catch (final Exception e) {
      throw e;
    }
    return null;
  }
}
