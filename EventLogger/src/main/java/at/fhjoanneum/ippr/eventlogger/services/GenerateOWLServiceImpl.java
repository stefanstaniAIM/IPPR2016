package at.fhjoanneum.ippr.eventlogger.services;


import at.fhjoanneum.ippr.eventlogger.helper.Arc;
import at.fhjoanneum.ippr.eventlogger.helper.Message;
import at.fhjoanneum.ippr.eventlogger.helper.XMLParserCommons;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Transactional(isolation = Isolation.READ_COMMITTED)
@Service
public class GenerateOWLServiceImpl implements GenerateOWLService {

  private static final Logger LOG = LoggerFactory.getLogger(GenerateOWLServiceImpl.class);
  private static final String standardPassOntUri = "&standard-pass-ont;";
  private String ontologyUri;

  @Override
  public StreamResult generateOWL(String processModelName, Map<String, String> petriNets)
          throws Exception {
    final DocumentBuilderFactory resultDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
    final DocumentBuilder resultDocumentBuilder = resultDocumentBuilderFactory.newDocumentBuilder();
    final Document resultDocument = resultDocumentBuilder.newDocument();
    final String date = DateTime.now().toString("ddMMyyyy-HHmm");
    ontologyUri = "http://fh-joanneum.at/aim/s-bpm/processmodels/"+date+"/";
    HashMap<String, Node> subjectNameToSubjectNodeMap = new HashMap<>();
    HashMap<String, Node> subjectNameToMessageExchangeListNodeMap = new HashMap<>();
    HashMap<String, Node> subjectNameToBehaviorNodeMap = new HashMap<>();
    HashMap<String, Node> messageNameToMessageNodeMap = new HashMap<>();
    HashMap<String, Node> messageNameToMessageExchangeNodeMap = new HashMap<>();

    Node rdfNode = getOWLSkeleton(resultDocument, resultDocumentBuilder);
    Node processModelNode = createNamedIndividual(resultDocument, processModelName, "PASSProcessModel", ontologyUri, processModelName);
    rdfNode.appendChild(processModelNode);

    Node sidNode = createNamedIndividual(resultDocument, "SID_1", "ModelLayer", "SID_1", "SID_1");
    rdfNode.appendChild(sidNode);
    addPriorityNumberElement(resultDocument, sidNode);

    addContainsElement(resultDocument, processModelNode, sidNode);

    //First create nodes based on name that will be referenced when looping over the PNML content
    petriNets.keySet().forEach(name -> {
      String subjectIdentifier = "SID_1_FullySpecifiedSingleSubject_"+name;
      Node subjectNode = createNamedIndividual(resultDocument, subjectIdentifier, "FullySpecifiedSingleSubject", subjectIdentifier, name);
      rdfNode.appendChild(subjectNode);
      addContainsElement(resultDocument, Arrays.asList(processModelNode, sidNode), subjectNode);
      addMaximumSubjectInstanceRestrictionElement(resultDocument, subjectNode);

      subjectNameToSubjectNodeMap.put(name, subjectNode);

      Node behaviorNode = createNamedIndividual(resultDocument, "SBD_"+subjectIdentifier, "SubjectBehavior", "SBD_"+subjectIdentifier, "SBD: "+name);
      rdfNode.appendChild(behaviorNode);
      addContainsElement(resultDocument, Arrays.asList(processModelNode, sidNode), behaviorNode);
      addResourceElement(resultDocument, subjectNode, behaviorNode, "containsBehavior");
      addResourceElement(resultDocument, subjectNode, behaviorNode, "containsBaseBehavior");
      subjectNameToBehaviorNodeMap.put(name, behaviorNode);

      String messageExchangeListIdentifier = "MessageExchangeList_on_SID_1_StandardMessageConnector_"+name;
      Node messageExchangeListNode = createNamedIndividual(resultDocument, messageExchangeListIdentifier, "MessageExchangeList", messageExchangeListIdentifier, "SID_1_StandardMessageConnector_"+name);
      rdfNode.appendChild(messageExchangeListNode);
      addContainsElement(resultDocument, processModelNode, messageExchangeListNode);

      subjectNameToMessageExchangeListNodeMap.put(name, messageExchangeListNode);
    });

    //Then create nodes based on content
    int messageId = 1;
    int stateId = 1;
    int transitionId = 1;
    int messageConnectorId = 1;

    for (Map.Entry<String, String> entry : petriNets.entrySet()) {
      String name = entry.getKey();
      String pnmlContent = entry.getValue();

      try {
        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        final InputSource input = new InputSource(new StringReader(pnmlContent));
        final Document document = documentBuilderFactory.newDocumentBuilder().parse(input);
        final Element root = document.getDocumentElement();
        final NodeList nets = root.getElementsByTagName("net");

        for (int temp = 0; temp < nets.getLength(); temp++) {
          final Node netNode = nets.item(temp);
          if (netNode.getNodeType() == Node.ELEMENT_NODE) {
            final Element net = (Element) netNode;

            HashSet<String> allPlaces = getAllPlaceIds(net);
            HashMap<String, List<Message>> placeIdToMessagesMap = getPlaceIdToMessagesMap(net);

            for (Map.Entry<String, List<Message>> placeToMessageEntry : placeIdToMessagesMap.entrySet()) {
              List<Message> messages = placeToMessageEntry.getValue();
              for(Message message : messages) {
                String messageName = "Message: "+message.getName() + " From: "+message.getSender() + " To: "+message.getRecipient();
                if(!messageNameToMessageNodeMap.containsKey(messageName)){
                  Node messageNode = createNamedIndividual(resultDocument, "message_"+messageId, "MessageSpecification", "message_"+messageId, message.getName());
                  rdfNode.appendChild(messageNode);
                  addContainsElement(resultDocument, Arrays.asList(processModelNode, sidNode), messageNode);

                  Node payloadDescriptionNode = createNamedIndividual(resultDocument, "payload_description_of_message_"+messageId, "PayloadDescription", "payload_description_of_message_"+messageId, "payload_description_of_message_"+messageId);
                  rdfNode.appendChild(payloadDescriptionNode);
                  addResourceElement(resultDocument, messageNode, payloadDescriptionNode, "containsPayloadDescription");

                  Node messageExchangeNode = createNamedIndividual(resultDocument, "SID_1_StandardMessageConnector_"+messageConnectorId+"_message_"+messageId, "MessageExchange", "SID_1_StandardMessageConnector_"+messageConnectorId+"_message_"+messageId, messageName);
                  rdfNode.appendChild(messageExchangeNode);
                  addResourceElement(resultDocument, messageExchangeNode, subjectNameToSubjectNodeMap.get(message.getSender()), "hasSender");
                  addResourceElement(resultDocument, messageExchangeNode, subjectNameToSubjectNodeMap.get(message.getRecipient()), "hasReceiver");
                  addResourceElement(resultDocument, messageExchangeNode, messageNode, "hasMessageType");
                  addContainsElement(resultDocument, Arrays.asList(processModelNode, sidNode), messageExchangeNode);

                  messageNameToMessageExchangeNodeMap.put(messageName, messageExchangeNode);
                  messageNameToMessageNodeMap.put(messageName, messageNode);
                  messageId++;
                  messageConnectorId++;
                }

                if(message.getSender().equals(name)){
                  addContainsElement(resultDocument, subjectNameToMessageExchangeListNodeMap.get(name), messageNameToMessageExchangeNodeMap.get(messageName));
                }
              }
            }

            final HashMap<String, Node> transitionIdToStateNodeMap = new HashMap<>();
            final HashMap<String, Node> transitionIdToActionNodeMap = new HashMap<>();
            final HashMap<String, String> doStatesIdToNameMap = new HashMap<>();
            final HashMap<String, String> sendStatesIdToNameMap = new HashMap<>();
            final HashMap<String, Arc> pnmlArcIdMap = getArcIdMap(net);
            final List<Arc> pnmlArcs = new ArrayList<>(pnmlArcIdMap.values());

            HashMap<String, Message> messagePlaceIdToMessageMap = new HashMap<>();
            for (Map.Entry<String, List<Message>> placeEntry : placeIdToMessagesMap.entrySet()) {
              placeEntry.getValue().forEach(pe -> messagePlaceIdToMessageMap.put(pe.getMessagePlaceId(), pe));
            }
            HashSet<Arc> directLinksBetweenTransitions = getDirectLinksBetweenTransitions(allPlaces, messagePlaceIdToMessageMap.keySet(), pnmlArcs);

            //First create all OWL states based on the PNML transitions, since the states will be referenced afterwards
            final HashMap<String, Node> stateNamesToStateNodeMap = new HashMap<>();
            final HashMap<String, Node> stateNamesToActionNodeMap = new HashMap<>();
            final HashMap<String, String> transitions = XMLParserCommons.getTransitionNameIdMap(net);
            for (Map.Entry<String, String> transitionEntry : transitions.entrySet()) {
              String transitionName = transitionEntry.getKey();
              String transitionIdentifier = transitionEntry.getValue();

              String stateType = getStateType(pnmlArcs, transitionIdentifier, placeIdToMessagesMap);
              if(stateType.equals("DoState")){
                doStatesIdToNameMap.put(transitionIdentifier, transitionName);
              } else if (stateType.equals("SendState")){
                sendStatesIdToNameMap.put(transitionIdentifier, transitionName);
              }

              Node stateNode;
              Node actionNode;

              if(stateNamesToStateNodeMap.containsKey(transitionName)){
                stateNode = stateNamesToStateNodeMap.get(transitionName);
                actionNode = stateNamesToActionNodeMap.get(transitionName);
              } else {
                stateNode = createNamedIndividual(resultDocument, "SBD_"+name+"_"+stateType+"_"+stateId, stateType, "SBD_"+name+"_"+stateType+"_"+stateId, transitionName);
                rdfNode.appendChild(stateNode);
                addHasFunctionSpecificationElement(resultDocument, stateNode, stateType);
                addContainsElement(resultDocument, subjectNameToBehaviorNodeMap.get(name), stateNode);

                String actionIdentifier = "action_of_SBD_"+name+"_"+stateType+"_"+stateId;
                actionNode = createNamedIndividual(resultDocument, actionIdentifier, "Action", actionIdentifier, actionIdentifier);
                rdfNode.appendChild(actionNode);
                addContainsElement(resultDocument, actionNode, stateNode);
                addContainsElement(resultDocument, subjectNameToBehaviorNodeMap.get(name), actionNode);


                stateNamesToStateNodeMap.put(transitionName, stateNode);
                stateNamesToActionNodeMap.put(transitionName, actionNode);
                stateId++;
              }

              if(isInitialState(directLinksBetweenTransitions, transitionIdentifier)){
                addInitialStateElement(resultDocument, stateNode);
                addResourceElement(resultDocument, subjectNameToBehaviorNodeMap.get(name), stateNode, "hasInitialState");
              } else if(isEndState(directLinksBetweenTransitions, transitionIdentifier)){
                addEndStateElement(resultDocument, stateNode);
                addResourceElement(resultDocument, subjectNameToBehaviorNodeMap.get(name), stateNode, "hasEndState");
              }

              transitionIdToStateNodeMap.put(transitionIdentifier, stateNode);
              transitionIdToActionNodeMap.put(transitionIdentifier, actionNode);
            }

            //Then create OWL transitions, that reference the states created before
            for(Arc arc : directLinksBetweenTransitions){
              HashMap<String, List<Node>> transitionNodes = new HashMap<>();
              if(doStatesIdToNameMap.containsKey(arc.getSource())){
                String transitionName = doStatesIdToNameMap.get(arc.getSource());
                Node transitionNode = createNamedIndividual(resultDocument, "SBD_"+name+"_StandardTransition_"+transitionId, "StandardTransition", "SBD_"+name+"_StandardTransition_"+transitionId, transitionName+" done");
                transitionNodes.put(arc.getSource(), Arrays.asList(transitionNode));
                transitionId++;
              } else if(sendStatesIdToNameMap.containsKey(arc.getSource())){
                List<Message> messages = new ArrayList<>();
                arc.getRefersToMessagePlaceIds().forEach(mpId -> messages.add(messagePlaceIdToMessageMap.get(mpId)));
                Node transitionNode;

                for(Message message : messages){
                  if (!message.getRecipient().equals(name)){
                    transitionNode = createNamedIndividual(resultDocument, "SBD_"+name+"_SendTransition_"+transitionId, "SendTransition", "SBD_"+name+"_SendTransition_"+transitionId, "To: "+message.getRecipient()+" Msg: "+message.getName());
                    String transitionConditionLabel = "sendTransitionCondition_"+"SBD_"+name+"_SendTransition_"+transitionId;
                    Node transitionConditionNode = createNamedIndividual(resultDocument, transitionConditionLabel, "SendTransitionCondition", transitionConditionLabel, transitionConditionLabel);
                    rdfNode.appendChild(transitionConditionNode);
                    addResourceElement(resultDocument, transitionNode, transitionConditionNode, "hasTransitionCondition");

                    String messageIdentifier = "Message: "+message.getName() + " From: "+message.getSender() + " To: "+message.getRecipient();
                    addResourceElement(resultDocument, transitionNode, messageNameToMessageExchangeNodeMap.get(messageIdentifier), "refersTo");

                    addToMapList(transitionNodes, arc.getSource(), transitionNode);
                    transitionId++;
                  }
                }

              } else {
                List<Message> messages = new ArrayList<>();
                arc.getRefersToMessagePlaceIds().forEach(mpId -> messages.add(messagePlaceIdToMessageMap.get(mpId)));
                Node transitionNode;

                for(Message message : messages){
                  if (!message.getSender().equals(name)){
                    transitionNode = createNamedIndividual(resultDocument, "SBD_"+name+"_ReceiveTransition_"+transitionId, "ReceiveTransition", "SBD_"+name+"_ReceiveTransition_"+transitionId, "From: "+message.getSender()+" Msg: "+message.getName());

                    String messageIdentifier = "Message: "+message.getName() + " From: "+message.getSender() + " To: "+message.getRecipient();
                    addResourceElement(resultDocument, transitionNode, messageNameToMessageExchangeNodeMap.get(messageIdentifier), "refersTo");

                    addToMapList(transitionNodes, arc.getSource(), transitionNode);
                    transitionId++;
                  }
                }
              }

              transitionNodes.forEach((id, nodes) -> {
                nodes.forEach(transitionNode -> {
                  rdfNode.appendChild(transitionNode);
                  addPriorityNumberElement(resultDocument, transitionNode);
                  Node source = transitionIdToStateNodeMap.get(arc.getSource());
                  Node target = transitionIdToStateNodeMap.get(arc.getTarget());

                  addResourceElement(resultDocument, transitionNode, source, "hasSourceState");
                  addResourceElement(resultDocument, transitionNode, target, "hasTargetState");
                  addContainsElement(resultDocument, Arrays.asList(subjectNameToBehaviorNodeMap.get(name), transitionIdToActionNodeMap.get(id)), transitionNode);
                });
              });
            }
          }
        }
      } catch (final Exception e) {
        LOG.error(e.getMessage());
        LOG.error("Exception while generating OWL");
      }
    }

    StreamResult documentResult;
    final DOMSource source = new DOMSource(resultDocument);

    Writer writer = new StringWriter();
    documentResult = new StreamResult(writer);
    final TransformerFactory transformerFactory = TransformerFactory.newInstance();
    final Transformer transformer = transformerFactory.newTransformer();
    transformer.transform(source, documentResult);


    //This is necessary since inline doctype is erased by the transformer Source: https://stackoverflow.com/questions/3509860/how-can-i-retain-doctype-info-in-xml-documnet-during-read-write-process
    String docType =  "<!DOCTYPE rdf:RDF [ " +
            "    <!ENTITY owl \"http://www.w3.org/2002/07/owl#\" >" +
            "    <!ENTITY xsd \"http://www.w3.org/2001/XMLSchema#\" >" +
            "    <!ENTITY rdfs \"http://www.w3.org/2000/01/rdf-schema#\" >" +
            "    <!ENTITY abstract-pass-ont \"http://www.imi.kit.edu/abstract-pass-ont#\" >" +
            "    <!ENTITY standard-pass-ont \"http://www.i2pm.net/standard-pass-ont#\" >" +
            "    <!ENTITY rdf \"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" >" +
            "]>";

    String documentString = writer.toString();
    documentString = documentString.replaceFirst("<rdf:RDF", docType+"<rdf:RDF");
    documentString = documentString.replaceAll("&amp;", "&");

    Writer resultWriter = new StringWriter();
    resultWriter.write(documentString);
    StreamResult result = new StreamResult(resultWriter);
    return result;

  }

  private Node getOWLSkeleton(Document doc, DocumentBuilder builder) throws IOException, SAXException {
    String skeleton = "<?xml version=\"1.0\"?>" +
            "" +
            "<rdf:RDF xmlns:abstract-pass-ont=\"http://www.imi.kit.edu/abstract-pass-ont#\" xmlns:standard-pass-ont=\"http://www.i2pm.net/standard-pass-ont#\" xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns:owl=\"http://www.w3.org/2002/07/owl#\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema#\" xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\" xmlns=\"" + ontologyUri + "\">" +
            "	<owl:Ontology rdf:about=\""+ontologyUri+"\">" +
            "       <owl:versionIRI rdf:resource=\""+ontologyUri+"\"></owl:versionIRI>" +
            "		<owl:imports rdf:resource=\"http://www.imi.kit.edu/abstract-pass-ont\"></owl:imports>" +
            "		<owl:imports rdf:resource=\"http://www.i2pm.net/standard-pass-ont\"></owl:imports>" +
            "	</owl:Ontology>" +
            "</rdf:RDF>";

    Document doc2 = builder.parse(new ByteArrayInputStream(skeleton.getBytes()));
    Node node = doc.importNode(doc2.getDocumentElement(), true);
    doc.appendChild(node);
    return node;
  }

  private Node createNamedIndividual(Document doc, String aboutName, String type, String id, String label) {
    final Element namedIndividual = doc.createElement("owl:NamedIndividual");
    namedIndividual.setAttribute("rdf:about", ontologyUri+"#"+aboutName);

    final Element rdfType = doc.createElement("rdf:type");
    rdfType.setAttribute("rdf:resource", standardPassOntUri+type);
    namedIndividual.appendChild(rdfType);

    final Element componentId = doc.createElement("standard-pass-ont:hasModelComponentID");
    componentId.setAttribute("rdf:datatype", "&xsd;string");
    componentId.appendChild(doc.createTextNode(id));
    namedIndividual.appendChild(componentId);

    final Element componentLabel = doc.createElement("standard-pass-ont:hasModelComponentLabel");
    componentLabel.setAttribute("xml:lang", "en");
    componentLabel.appendChild(doc.createTextNode(label));
    namedIndividual.appendChild(componentLabel);

    return namedIndividual;
  }

  private void addContainsElement(Document doc, Node addToNode, Node resourceNode){
    addResourceElement(doc, addToNode, resourceNode, "contains");
  }

  private void addContainsElement(Document doc, List<Node> addToNodes, Node resourceNode){
    addToNodes.forEach(node -> addContainsElement(doc, node, resourceNode));
  }

  private void addPriorityNumberElement(Document doc, Node addToNode) {
    final Element priorityNumber = doc.createElement("standard-pass-ont:hasPriorityNumber");
    priorityNumber.setAttribute("rdf:datatype", "http://www.w3.org/2001/XMLSchema#positiveInteger");
    priorityNumber.appendChild(doc.createTextNode("1"));
    addToNode.appendChild(priorityNumber);
  }

  private void addMaximumSubjectInstanceRestrictionElement(Document doc, Node addToNode) {
    final Element restriction = doc.createElement("standard-pass-ont:hasMaximumSubjectInstanceRestriction");
    restriction.setAttribute("rdf:datatype", "http://www.w3.org/2001/XMLSchema#integer");
    restriction.appendChild(doc.createTextNode("1"));
    addToNode.appendChild(restriction);
  }

  private void addHasFunctionSpecificationElement(Document doc, Node addToNode, String stateType) {
    final Element specification = doc.createElement("standard-pass-ont:hasFunctionSpecification");

    String type = "Do1_EnvoironmentChoice";
    if (stateType.equals("SendState")) {
      type = "Send";
    } else if (stateType.equals("ReceiveState")) {
      type = "Receive";
    }

    specification.setAttribute("rdf:resource", "http://www.i2pm.net/standard-pass-ont#DefaultFunction"+type);
    addToNode.appendChild(specification);
  }

  private void addResourceElement(Document doc, Node addToNode, Node resourceNode, String tag){
    final Element resourceElement = doc.createElement("standard-pass-ont:"+tag);
    resourceElement.setAttribute("rdf:resource", resourceNode.getAttributes().getNamedItem("rdf:about").getNodeValue());
    addToNode.appendChild(resourceElement);
  }

  private void addInitialStateElement(Document doc, Node addToNode){
    final Element element = doc.createElement("rdf:type");
    element.setAttribute("rdf:resource", standardPassOntUri+"InitialState");
    addToNode.appendChild(element);
  }

  private void addEndStateElement(Document doc, Node addToNode){
    final Element element = doc.createElement("rdf:type");
    element.setAttribute("rdf:resource", standardPassOntUri+"EndState");
    addToNode.appendChild(element);
  }

  private HashMap<String, List<Message>> getPlaceIdToMessagesMap(final Element net) {
    HashMap<String, List<Message>> placeIdToMessagesMap = new HashMap<>();
    final NodeList placeNodes = net.getElementsByTagName("place");
    for (int i = 0; i < placeNodes.getLength(); i++) {
      final Element placeNode = (Element) placeNodes.item(i);
      final NodeList toolspecificNodes = placeNode.getElementsByTagName("toolspecific");
      String id = placeNode.getAttributes().getNamedItem("id").getNodeValue();

      if(toolspecificNodes.getLength() > 0){
        NamedNodeMap attributes = toolspecificNodes.item(0).getAttributes();
        String tool = attributes.getNamedItem("tool").getNodeValue();

        if(tool.equals("SBPM")){
          String type = attributes.getNamedItem("type").getNodeValue();
          String message = attributes.getNamedItem("message").getNodeValue();
          String recipient = attributes.getNamedItem("recipient").getNodeValue();
          String sender = attributes.getNamedItem("sender").getNodeValue();

          Message messageObj = new Message(message, recipient, sender, id);

          if(type.equals("receive")){
            id = attributes.getNamedItem("actualTargetId").getNodeValue();
          }

          if(placeIdToMessagesMap.containsKey(id)){
            placeIdToMessagesMap.get(id).add(messageObj);
          } else {
            List<Message> newList = new ArrayList<>();
            newList.add(messageObj);
            placeIdToMessagesMap.put(id, newList);
          }
        }
      }
    }
    return placeIdToMessagesMap;
  }

  private HashSet<String> getAllPlaceIds(final Element net) {
    HashSet<String> allPlaceIds = new HashSet<>();
    final NodeList placeNodes = net.getElementsByTagName("place");
    for (int i = 0; i < placeNodes.getLength(); i++) {
      final Element placeNode = (Element) placeNodes.item(i);
      String id = placeNode.getAttributes().getNamedItem("id").getNodeValue();
      allPlaceIds.add(id);
    }
    return allPlaceIds;
  }

  private HashSet<Arc> getDirectLinksBetweenTransitions(HashSet<String> allPlaceIds, Set<String> allMessagePlaceIds, List<Arc> pnmlArcs){
    HashMap<String, List<String>> sourcePlaceIdToTargetTransitionIds = new HashMap<>();
    HashMap<String, List<String>> targetPlaceIdToSourceTransitionIds = new HashMap<>();
    HashMap<String, List<String>> transitionIdRefersToMessageIds = new HashMap<>();
    HashSet<Arc> directLinksBetweenTransitions = new HashSet<>();
    int arcId = 1;
    for(Arc arc : pnmlArcs){
      if(allMessagePlaceIds.contains(arc.getTarget())){
        //arc is for sending
        addToMapList(transitionIdRefersToMessageIds, arc.getSource(), arc.getTarget());
      }
      else if(allMessagePlaceIds.contains(arc.getTarget()) || allMessagePlaceIds.contains(arc.getSource())){
        //arc is for receiving
        addToMapList(transitionIdRefersToMessageIds, arc.getTarget(), arc.getSource());
      } else if(allPlaceIds.contains(arc.getSource())){
        //arc is from place (source) to transition (target)
        if(sourcePlaceIdToTargetTransitionIds.containsKey(arc.getSource())){
          sourcePlaceIdToTargetTransitionIds.get(arc.getSource()).add(arc.getTarget());
        } else {
          List<String> newList = new ArrayList<>();
          newList.add(arc.getTarget());
          sourcePlaceIdToTargetTransitionIds.put(arc.getSource(), newList);
        }
      } else if(allPlaceIds.contains(arc.getTarget())){
        //arc is from transition (source) to place (target)
        if(targetPlaceIdToSourceTransitionIds.containsKey(arc.getTarget())) {
          targetPlaceIdToSourceTransitionIds.get(arc.getTarget()).add(arc.getSource());
        } else {
          List<String> newList = new ArrayList<>();
          newList.add(arc.getSource());
          targetPlaceIdToSourceTransitionIds.put(arc.getTarget(), newList);
        }
      }
    }

    for (Map.Entry<String, List<String>> entry : targetPlaceIdToSourceTransitionIds.entrySet()) {
      List<String> sourceTransitionIds = entry.getValue();
      List<String> targetTransitionIds = sourcePlaceIdToTargetTransitionIds.get(entry.getKey());

      if(targetTransitionIds != null) {
        for(String targetTransitionId : targetTransitionIds){
          for(String sourceTransitionId : sourceTransitionIds){
            List<String> refersToMessageIds = new ArrayList<>();
            if(transitionIdRefersToMessageIds.containsKey(sourceTransitionId)){
              refersToMessageIds = transitionIdRefersToMessageIds.get(sourceTransitionId);
            } else if (transitionIdRefersToMessageIds.containsKey(targetTransitionId)){
              refersToMessageIds = transitionIdRefersToMessageIds.get(targetTransitionId);
            }

            Arc arc = new Arc(String.valueOf(arcId), sourceTransitionId, targetTransitionId, refersToMessageIds);

            directLinksBetweenTransitions.add(arc);
            arcId++;
          }
        }
      }
    }

    return directLinksBetweenTransitions;
  }

  private <K,V> void addToMapList(HashMap<K,List<V>> map, K key, V value){
    if(map.containsKey(key)){
      map.get(key).add(value);
    } else {
      List<V> newList = new ArrayList<>();
      newList.add(value);
      map.put(key, newList);
    }
  }

  private String getStateType(List<Arc> arcs, String transitionId, HashMap<String, List<Message>> placeIdToMessageMap){
    String stateType = "DoState";

    Predicate<Arc> sourcePredicate = p -> p.getSource().equals(transitionId);
    List<Arc> arcsWithTransitionIdAsSource = arcs.stream().filter(sourcePredicate).collect(Collectors.toList());
    for(Arc arc : arcsWithTransitionIdAsSource) {
      if(placeIdToMessageMap.containsKey(arc.getTarget())){
        stateType = "SendState";
        break;
      }
    }

    if(stateType.equals("DoState")){
      Predicate<Arc> targetPredicate = p -> p.getTarget().equals(transitionId);
      List<Arc> arcsWithTransitionIdAsTarget = arcs.stream().filter(targetPredicate).collect(Collectors.toList());
      for(Arc arc : arcsWithTransitionIdAsTarget) {
        if(placeIdToMessageMap.containsKey(arc.getTarget())){
          stateType = "ReceiveState";
          break;
        }
      }
    }

    return stateType;
  }

  private boolean isInitialState(Set<Arc> arcs, String transitionId){
    Predicate<Arc> targetPredicate = p -> p.getTarget().equals(transitionId);
    return arcs.stream().filter(targetPredicate).count() < 1;
  }

  private boolean isEndState(Set<Arc> arcs, String transitionId){
    Predicate<Arc> sourcePredicate = p -> p.getSource().equals(transitionId);
    return arcs.stream().filter(sourcePredicate).count() < 1;
  }

  private HashMap<String, Arc> getArcIdMap(final Element net) {
    final HashMap<String, Arc> pnmlArcIdMap = new HashMap<>();
    final NodeList arcNodes = net.getElementsByTagName("arc");

    for (int i = 0; i < arcNodes.getLength(); i++) {
      final Element arcNode = (Element) arcNodes.item(i);
      String id = arcNode.getAttributes().getNamedItem("id").getNodeValue();
      String source = arcNode.getAttributes().getNamedItem("source").getNodeValue();
      String target = arcNode.getAttributes().getNamedItem("target").getNodeValue();

      pnmlArcIdMap.put(id, new Arc(id, source, target));
    }

    return pnmlArcIdMap;
  }
}