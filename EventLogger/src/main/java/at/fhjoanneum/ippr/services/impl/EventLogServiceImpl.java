package at.fhjoanneum.ippr.pmstorage.services.impl;


import at.fhjoanneum.ippr.commons.dto.processengine.EventLoggerDTO;
import at.fhjoanneum.ippr.persistence.EventLog;
import at.fhjoanneum.ippr.persistence.EventLogRepository;
import at.fhjoanneum.ippr.pmstorage.services.EventLogService;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.concurrent.Future;


@Transactional(isolation = Isolation.READ_COMMITTED)
@Service
public class EventLogServiceImpl implements EventLogService {

    private static final Logger LOG = LoggerFactory.getLogger(EventLogServiceImpl.class);

    @Autowired
    private EventLogRepository eventLogRepository;

    @Async
    @Override
    public Future<List<EventLoggerDTO>> getEventLogForProcessModel(int processModelId) {
        final List<EventLog> results = eventLogRepository.getEventLogForProcessModel(processModelId);
        final List<EventLoggerDTO> eventLog = createEventLoggerDTO(results);
        return new AsyncResult<List<EventLoggerDTO>>(eventLog);
    }

    private static List<EventLoggerDTO> createEventLoggerDTO(final List<EventLog> results) {
        final List<EventLoggerDTO> eventLog = Lists.newArrayList();

        results.forEach(event -> {
            final EventLoggerDTO dto =
                    new EventLoggerDTO(event.getCaseId(), event.getProcessModelId(), event.getTimestamp(), event.getActivity(), event.getResource(), event.getState(), event.getMessageType());
            eventLog.add(dto);
        });

        return eventLog;
    }

    @Override
    public StreamResult manipulatePNML(String pnmlContent, String csvLog) {
        StreamResult result = new StreamResult();
        /*XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        try {
            XMLStreamReader pnmlReader = inputFactory.createXMLStreamReader(csvStream);
            pnmlReader.getText();
        } catch(Exception e){
            throw e;
        }*/
        try {
            /*https://stackoverflow.com/questions/6445828/how-do-i-append-a-node-to-an-existing-xml-file-in-java*/
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            InputSource input = new InputSource(new StringReader(pnmlContent));
            Document document = documentBuilderFactory.newDocumentBuilder().parse(input);
            Element root = document.getDocumentElement();
            NodeList nets = root.getElementsByTagName("net");
            for (int temp = 0; temp < nets.getLength(); temp++) {
                Node netNode = nets.item(temp);
                if (netNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element net = (Element) netNode;
                    NodeList places = net.getElementsByTagName("place");
                    //String placeId = places.item(0).getAttributes().getNamedItem("id").getNodeValue();
                    NodeList transitions = net.getElementsByTagName("transition");
                    NodeList arcs = net.getElementsByTagName("arc");

                    //Custom Place
                    Element newPlace = document.createElement("place");
                    net.appendChild(newPlace);

                    //Id
                    newPlace.setAttribute("id", "cp1");

                    //Name
                    Element name = document.createElement("name");
                    newPlace.appendChild(name);

                    //Name Text
                    Element text = document.createElement("text");
                    text.appendChild(document.createTextNode("Custom Place 1"));
                    name.appendChild(text);

                    //Graphics
                    Element graphics = document.createElement("graphics");
                    newPlace.appendChild(graphics);

                    //Graphics Position
                    Element position = document.createElement("position");
                    position.setAttribute("x", "0");
                    position.setAttribute("y", "0");
                    graphics.appendChild(position);

                    //Graphics Dimension
                    Element dimension = document.createElement("dimension");
                    dimension.setAttribute("x", "12.5");
                    dimension.setAttribute("y", "12.5");
                    graphics.appendChild(position);
                }

                DOMSource source = new DOMSource(document);

                result = new StreamResult(new StringWriter());
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.transform(source, result);
            }
        } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
            LOG.error("Exception while manipulating PNML");
        }

        return result;
    }
}
