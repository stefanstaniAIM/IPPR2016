package at.fhjoanneum.ippr.eventlogger.services;


import at.fhjoanneum.ippr.eventlogger.helper.Message;
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
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;


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
      addContainsElements(resultDocument, Arrays.asList(processModelNode, sidNode), subjectNode);
      addMaximumSubjectInstanceRestrictionElement(resultDocument, subjectNode);

      subjectNameToSubjectNodeMap.put(name, subjectNode);

      Node behaviorNode = createNamedIndividual(resultDocument, "SBD_"+subjectIdentifier, "SubjectBehavior", "SBD_"+subjectIdentifier, "SBD: "+name);
      rdfNode.appendChild(behaviorNode);
      addContainsElements(resultDocument, Arrays.asList(processModelNode, sidNode), behaviorNode);
      addResourceElement(resultDocument, subjectNode, behaviorNode, "containsBehavior");
      addResourceElement(resultDocument, subjectNode, behaviorNode, "containsBaseBehavior");

      String messageExchangeListIdentifier = "MessageExchangeList_on_SID_1_StandardMessageConnector_"+name;
      Node messageExchangeListNode = createNamedIndividual(resultDocument, messageExchangeListIdentifier, "MessageExchangeList", messageExchangeListIdentifier, "SID_1_StandardMessageConnector_"+name);
      rdfNode.appendChild(messageExchangeListNode);
      addContainsElement(resultDocument, processModelNode, messageExchangeListNode);

      subjectNameToMessageExchangeListNodeMap.put(name, messageExchangeListNode);
    });

    //Then create nodes based on content
    int mId = 1;
    petriNets.forEach((name, pnmlContent) -> {
      //find send messages of subject and create ;MessageSpecification and ;MessageExchangeList and ;PayloadDescription and ;StandardMessageExchange

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
            //final HashMap<String, String> transitions = XMLParserCommons.getTransitionNameIdMap(net);
            final HashSet<Message> messagesToSend = getMessages(net);
            for (Message message : messagesToSend) {
              Node messageNode = createNamedIndividual(resultDocument, "message_"+mId, "MessageSpecification", "message_"+mId, message.getName());
              rdfNode.appendChild(messageNode);
              addContainsElements(resultDocument, Arrays.asList(subjectNameToMessageExchangeListNodeMap.get(name), processModelNode, sidNode), messageNode);

              Node payloadDescriptionNode = createNamedIndividual(resultDocument, "payload_description_of_message_"+mId, "PayloadDescription", "payload_description_of_message_"+mId, "payload_description_of_message_"+mId);
              rdfNode.appendChild(payloadDescriptionNode);
              addResourceElement(resultDocument, messageNode, payloadDescriptionNode, "ContainsPayloadDescription");

              Node standardMessageExchangeNode = createNamedIndividual(resultDocument, "SID_1_StandardMessageConnector_"+name+"_message_"+mId, "StandardMessageExchange", "SID_1_StandardMessageConnector_"+name+"_message_"+mId, "Message: "+message.getName() + " From: "+message.getSender() + " To: "+message.getRecipient());
              rdfNode.appendChild(standardMessageExchangeNode);
              addResourceElement(resultDocument, standardMessageExchangeNode, subjectNameToSubjectNodeMap.get(message.getSender()), "hasSender");
              addResourceElement(resultDocument, standardMessageExchangeNode, subjectNameToSubjectNodeMap.get(message.getRecipient()), "hasReceiver");
              addResourceElement(resultDocument, standardMessageExchangeNode, messageNode, "hasMessageType");
              addContainsElements(resultDocument, Arrays.asList(processModelNode, sidNode), standardMessageExchangeNode);

              mId++;
            }
          }
        }
      } catch (final Exception e) {
        LOG.error(e.getMessage());
        LOG.error("Exception while generating OWL");
      }
    });

    StreamResult result;
    final DOMSource source = new DOMSource(resultDocument);

    result = new StreamResult(new StringWriter());
    final TransformerFactory transformerFactory = TransformerFactory.newInstance();
    final Transformer transformer = transformerFactory.newTransformer();
    transformer.transform(source, result);
    return result;
  }

  private Node getOWLSkeleton(Document doc, DocumentBuilder builder) throws IOException, SAXException {
    String skeleton = "<?xml version=\"1.0\"?>" +
            "" +
            "<!DOCTYPE rdf:RDF [ " +
            "    <!ENTITY owl \"http://www.w3.org/2002/07/owl#\" >" +
            "    <!ENTITY xsd \"http://www.w3.org/2001/XMLSchema#\" >" +
            "    <!ENTITY rdfs \"http://www.w3.org/2000/01/rdf-schema#\" >" +
            "    <!ENTITY abstract-pass-ont \"http://www.imi.kit.edu/abstract-pass-ont#\" >" +
            "    <!ENTITY standard-pass-ont \"http://www.i2pm.net/standard-pass-ont#\" >" +
            "    <!ENTITY rdf \"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" >" +
            "]>" +
            "" +
            "<rdf:RDF xmlns:abstract-pass-ont=\"http://www.imi.kit.edu/abstract-pass-ont#\" xmlns:standard-pass-ont=\"http://www.i2pm.net/standard-pass-ont#\" xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns:owl=\"http://www.w3.org/2002/07/owl#\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema#\" xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\" xmlns=\"http://subjective-me.jimdo.com/s-bpm/processmodels/2017-07-28/Zeichenblatt-1\">" +
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

    final Element componentLabel = doc.createElement("standard-pass-ont:hasModelComponentLable");
    componentLabel.setAttribute("xml:lang", "en");
    componentLabel.appendChild(doc.createTextNode(label));
    namedIndividual.appendChild(componentLabel);

    return namedIndividual;
  }

  private void addContainsElement(Document doc, Node addToNode, Node resourceNode){
    addResourceElement(doc, addToNode, resourceNode, "contains");
  }

  private void addContainsElements(Document doc, List<Node> addToNodes, Node resourceNode){
    addToNodes.forEach(node -> addContainsElement(doc, node, resourceNode));
  }

  private void addPriorityNumberElement(Document doc, Node addToNode) {
    final Element priorityNumber = doc.createElement("standard-pass-ont:hasPriorityNumber");
    priorityNumber.setAttribute("rdf:datatype", "http://www.w3.org/2001/XMLSchema#positiveInteger");
    priorityNumber.appendChild(doc.createTextNode("1"));
    addToNode.appendChild(priorityNumber);
  }

  private void addMaximumSubjectInstanceRestrictionElement(Document doc, Node addToNode) {
    final Element priorityNumber = doc.createElement("standard-pass-ont:hasMaximumSubjectInstanceRestriction");
    priorityNumber.setAttribute("rdf:datatype", "http://www.w3.org/2001/XMLSchema#integer");
    priorityNumber.appendChild(doc.createTextNode("1"));
    addToNode.appendChild(priorityNumber);
  }

  private void addResourceElement(Document doc, Node addToNode, Node resourceNode, String tag){
    final Element resourceElement = doc.createElement("standard-pass-ont:"+tag);
    resourceElement.setAttribute("rdf:resource", resourceNode.getAttributes().getNamedItem("rdf:about").getNodeValue());
    addToNode.appendChild(resourceElement);
  }

  private HashSet<Message> getMessages(final Element net) {
    final HashSet<Message> messages = new HashSet<>();
    final NodeList placeNodes = net.getElementsByTagName("place");
    for (int i = 0; i < placeNodes.getLength(); i++) {
      final Element placeNode = (Element) placeNodes.item(i);
      final NodeList toolspecificNodes = placeNode.getElementsByTagName("toolspecific");

      if(toolspecificNodes.getLength() > 0){
        NamedNodeMap attributes = toolspecificNodes.item(0).getAttributes();
        String tool = attributes.getNamedItem("tool").getNodeValue();

        if(tool.equals("SBPM")){
          String type = attributes.getNamedItem("type").getNodeValue();

          if(type.equals("send")){
            String message = attributes.getNamedItem("message").getNodeValue();
            String recipient = attributes.getNamedItem("recipient").getNodeValue();
            String sender = attributes.getNamedItem("sender").getNodeValue();

            messages.add(new Message(message, recipient, sender));
          }
        }
      }
    }

    return messages;
  }


}
