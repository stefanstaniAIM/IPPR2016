package at.fhjoanneum.ippr.eventlogger.services;


import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


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

    Node rdfNode = getOWLSkeleton(resultDocument, resultDocumentBuilder);
    Node processModelNode = createNamedIndividual(resultDocument, processModelName, "PASSProcessModel", ontologyUri, processModelName);
    rdfNode.appendChild(processModelNode);

    Node sidNode = createNamedIndividual(resultDocument, "SID_1", "ModelLayer", "SID_1", "SID_1");
    rdfNode.appendChild(sidNode);
    addPriorityNumberElement(resultDocument, sidNode);

    addContainsElement(resultDocument, processModelNode, sidNode);

    petriNets.forEach((name, pnmlContent) -> {
      String identifier = "SID_1_FullySpecifiedSingleSubject_"+name;
      Node subjectNode = createNamedIndividual(resultDocument, identifier, "FullySpecifiedSingleSubject", identifier, name);
      rdfNode.appendChild(subjectNode);
      addContainsElement(resultDocument, Arrays.asList(processModelNode, sidNode), subjectNode);

      Node behaviorNode = createNamedIndividual(resultDocument, "SBD_"+identifier, "SubjectBehavior", "SBD_"+identifier, "SBD: "+name);
      rdfNode.appendChild(behaviorNode);
      addContainsElement(resultDocument, Arrays.asList(processModelNode, sidNode), behaviorNode);
      addContainsBehaviorElement(resultDocument, subjectNode, behaviorNode);

      //find send messages of subject and create ;MessageSpecification and ;MessageExchangeList and ;PayloadDescription and ;StandardMessageExchange


      /*try {
        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        final InputSource input = new InputSource(new StringReader(pnmlContent));
        final Document document = documentBuilderFactory.newDocumentBuilder().parse(input);
        final Element root = document.getDocumentElement();
        final NodeList nets = root.getElementsByTagName("net");
        for (int temp = 0; temp < nets.getLength(); temp++) {
          final Node netNode = nets.item(temp);
          if (netNode.getNodeType() == Node.ELEMENT_NODE) {
            final Element net = (Element) netNode;
            final HashMap<String, String> transitions = getTransitionNameIdMap(net);

          }
        }
      } catch (final Exception e) {
        LOG.error(e.getMessage());
        LOG.error("Exception while generating OWL");
      }*/
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
    Element containsElement = doc.createElement("standard-pass-ont:contains");
    containsElement.setAttribute("rdf:resource", resourceNode.getAttributes().getNamedItem("rdf:about").getNodeValue());
    addToNode.appendChild(containsElement);
  }

  private void addContainsBehaviorElement(Document doc, Node addToNode, Node resourceNode){
    Element containsElement = doc.createElement("standard-pass-ont:containsBehavior");
    containsElement.setAttribute("rdf:resource", resourceNode.getAttributes().getNamedItem("rdf:about").getNodeValue());
    addToNode.appendChild(containsElement);
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
}
