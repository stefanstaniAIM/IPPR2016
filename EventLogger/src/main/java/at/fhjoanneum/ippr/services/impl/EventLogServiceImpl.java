package at.fhjoanneum.ippr.pmstorage.services.impl;


import at.fhjoanneum.ippr.Helpers.LogKey;
import at.fhjoanneum.ippr.commons.dto.processengine.EventLoggerDTO;
import at.fhjoanneum.ippr.persistence.EventLogEntry;
import at.fhjoanneum.ippr.persistence.EventLogRepository;
import at.fhjoanneum.ippr.persistence.objects.model.enums.StateFunctionType;
import at.fhjoanneum.ippr.pmstorage.services.EventLogService;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;
import java.util.concurrent.Future;


@Transactional(isolation = Isolation.READ_COMMITTED)
@Service
public class EventLogServiceImpl implements EventLogService {

    private static final Logger LOG = LoggerFactory.getLogger(EventLogServiceImpl.class);

    @Autowired
    private EventLogRepository eventLogRepository;

    @Async
    @Override
    public Future<List<EventLoggerDTO>> getEventLogForProcessModelAndSubject(int processModelId, String subject) {
        final List<EventLogEntry> results = eventLogRepository.getEventLogForProcessModelAndSubject(processModelId, subject);
        final List<EventLoggerDTO> eventLog = createEventLoggerDTO(results);
        return new AsyncResult<List<EventLoggerDTO>>(eventLog);
    }

    private static List<EventLoggerDTO> createEventLoggerDTO(final List<EventLogEntry> results) {
        final List<EventLoggerDTO> eventLog = Lists.newArrayList();

        results.forEach(event -> {
            final EventLoggerDTO dto =
                    new EventLoggerDTO(event.getEventId(), event.getCaseId(), event.getProcessModelId(), event.getTimestamp(), event.getActivity(), event.getResource(), event.getState(), event.getMessageType());
            eventLog.add(dto);
        });

        return eventLog;
    }

    @Override
    public StreamResult manipulatePNML(String pnmlContent, String csvLog) throws Exception{
        StreamResult result = new StreamResult();

        try {
            LinkedHashMap<LogKey, EventLogEntry> logTriplets = parseCSV(csvLog);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            InputSource input = new InputSource(new StringReader(pnmlContent));
            Document document = documentBuilderFactory.newDocumentBuilder().parse(input);
            Element root = document.getDocumentElement();
            NodeList nets = root.getElementsByTagName("net");

            for (int temp = 0; temp < nets.getLength(); temp++) {
                Node netNode = nets.item(temp);
                if (netNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element net = (Element) netNode;
                    HashMap<String, String> transitions = getTransitions(net);

                    int numOfCustomPlaces = 1;
                    int numOfCustomArcs= 1;
                    Iterator<Map.Entry<LogKey, EventLogEntry>> it = logTriplets.entrySet().iterator();
                    Map.Entry<LogKey, EventLogEntry> afterReceiveStateEntry = null;
                    while (it.hasNext() || afterReceiveStateEntry != null) {
                        Map.Entry<LogKey, EventLogEntry> entry;
                        if (afterReceiveStateEntry != null) {
                            entry = afterReceiveStateEntry;
                        } else {
                            entry = it.next();
                        }

                        EventLogEntry eventLogEntry = entry.getValue();

                        String state = eventLogEntry.getState();
                        String activity = eventLogEntry.getActivity();
                        String messageType = eventLogEntry.getMessageType();

                        if (state.equals(StateFunctionType.SEND.name())) {
                            afterReceiveStateEntry = null;
                            String placeId = addPlace(document, net, numOfCustomPlaces++, messageType);
                            addArc(document, net, numOfCustomArcs++, transitions.get(activity), placeId, messageType) ;
                        } else if (state.equals(StateFunctionType.RECEIVE.name())) {
                            if (!it.hasNext()) {
                                throw(new Exception("Letzter Log-Eintrag darf kein Receive State sein!"));
                            }

                            Map.Entry<LogKey, EventLogEntry> nextEntry = it.next();
                            EventLogEntry nextEventLogEntry = nextEntry.getValue();

                            String nextActivity = nextEventLogEntry.getActivity();
                            String nextMessageType = nextEventLogEntry.getMessageType();

                            String placeId = addPlace(document, net, numOfCustomPlaces++, nextMessageType);
                            addArc(document, net, numOfCustomArcs++, placeId, transitions.get(nextActivity), nextMessageType) ;

                            afterReceiveStateEntry = nextEntry;
                        } else {
                            afterReceiveStateEntry = null;
                        }
                    }
                }

                DOMSource source = new DOMSource(document);

                result = new StreamResult(new StringWriter());
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.transform(source, result);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            LOG.error("Exception while manipulating PNML");
            throw(e);
        }

        return result;
    }

    private LinkedHashMap<LogKey, EventLogEntry> parseCSV(String csvLog) throws Exception {

        LinkedHashMap<LogKey, EventLogEntry> result = new LinkedHashMap<>();
        ICsvBeanReader beanReader = null;
        try {
            beanReader = new CsvBeanReader(new StringReader(csvLog), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

            // the header elements are used to map the values to the bean (names must match)
            final String[] header = beanReader.getHeader(true);
            final String[] uncapitalizedHeader = new String[header.length];
            for(int i = 0; i < header.length; i++)
            {
                uncapitalizedHeader[i] = StringUtils.uncapitalize(header[i]);
            }

            final CellProcessor[] processors = getProcessors();

            EventLogEntry eventLogEntry;
            while((eventLogEntry = beanReader.read(EventLogEntry.class, uncapitalizedHeader, processors)) != null) {

                LogKey key = new LogKey(eventLogEntry.getActivity(), eventLogEntry.getState(), eventLogEntry.getMessageType());
                if(!result.containsKey(key)) {
                    result.put(key, eventLogEntry);
                }
            }
        }
        finally {
            if(beanReader != null) {
                beanReader.close();
            }

            return result;
        }
    }

    private static CellProcessor[] getProcessors() {
        final CellProcessor[] processors = new CellProcessor[] {
                new NotNull(new ParseLong()), // EventId
                new NotNull(new ParseLong()), // CaseId
                new NotNull(), // Timestamp
                new NotNull(), // Activity
                new NotNull(), // Resource
                new NotNull(), // State
                new Optional() // MessageType
        };

        return processors;
    }
    private String addArc(Document document, Element net, int id, String sourceId, String targetId, String name) {
        //Custom Arc
        Element newArc = document.createElement("arc");
        net.appendChild(newArc);

        //Id
        String arcId = "ca"+id;
        newArc.setAttribute("id", arcId);

        //Source Id
        newArc.setAttribute("source", sourceId);

        //Target Id
        newArc.setAttribute("target", targetId);

        //Name
        Element nameElement = document.createElement("name");
        newArc.appendChild(nameElement);

        //Name Text
        Element nameText = document.createElement("text");
        nameText.appendChild(document.createTextNode(name));
        nameElement.appendChild(nameText);

        //Arctype
        Element arcTypeElement = document.createElement("arcType");
        newArc.appendChild(arcTypeElement);

        //Arctype Text
        Element arcTypeText = document.createElement("text");
        arcTypeText.appendChild(document.createTextNode("normal"));
        arcTypeElement.appendChild(arcTypeText);

        return arcId;
    }

    private String addPlace(Document document, Element net, int id, String name) {
        //Custom Place
        Element newPlace = document.createElement("place");
        net.appendChild(newPlace);

        //Id
        String placeId = "cp"+id;
        newPlace.setAttribute("id", placeId);

        //Name
        Element nameElement = document.createElement("name");
        newPlace.appendChild(nameElement);

        //Name Text
        Element text = document.createElement("text");
        text.appendChild(document.createTextNode(name));
        nameElement.appendChild(text);

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

        return placeId;
    }

    private HashMap<String, String> getTransitions(Element net) {
        HashMap<String, String> transitions = new HashMap<>();
        NodeList transitionNodes = net.getElementsByTagName("transition");
        for (int i = 0; i < transitionNodes.getLength(); i++) {
            Element transitionNode = (Element)transitionNodes.item(i);
            String id = transitionNode.getAttributes().getNamedItem("id").getNodeValue();
            NodeList nameNodes = transitionNode.getElementsByTagName("name");

            if(nameNodes.getLength() > 0) {
                Node nameNode = nameNodes.item(0);
                String name = nameNode.getChildNodes().item(0).getTextContent();
                transitions.put(name, id);
            }
        }

        return transitions;
    }
}
