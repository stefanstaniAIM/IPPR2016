package at.fhjoanneum.ippr.pmstorage.services.impl;


import at.fhjoanneum.ippr.commons.dto.processengine.EventLoggerDTO;
import at.fhjoanneum.ippr.persistence.EventLogEntry;
import at.fhjoanneum.ippr.persistence.EventLogRepository;
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
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileReader;
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
        final List<EventLogEntry> results = eventLogRepository.getEventLogForProcessModel(processModelId);
        final List<EventLoggerDTO> eventLog = createEventLoggerDTO(results);
        return new AsyncResult<List<EventLoggerDTO>>(eventLog);
    }

    private static List<EventLoggerDTO> createEventLoggerDTO(final List<EventLogEntry> results) {
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
        try {
            parseCSV(csvLog);
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

                    createPlace(document, net);
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
        }

        return result;
    }

    /*private void parseCSV(String csvLog) {
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";

        try (BufferedReader br = new BufferedReader(new StringReader(csvLog))) {
            while ((line = br.readLine()) != null) {
                String[] country = line.split(cvsSplitBy);

                System.out.println("Country [code= " + country[4] + " , name=" + country[5] + "]");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    private void parseCSV(String csvLog) throws Exception {
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
            while( (eventLogEntry = beanReader.read(EventLogEntry.class, uncapitalizedHeader, processors)) != null ) {
                System.out.println(String.format("%s, %s, %s, %s, %s, %s",
                        eventLogEntry.getCaseId(), eventLogEntry.getTimestamp(), eventLogEntry.getActivity(),
                        eventLogEntry.getResource(), eventLogEntry.getState(), eventLogEntry.getMessageType()));
                //https://stackoverflow.com/questions/14148331/how-to-get-a-hashmap-value-with-three-values
            }
        }
        finally {
            if( beanReader != null ) {
                beanReader.close();
            }
        }
    }

    private static CellProcessor[] getProcessors() {
        final CellProcessor[] processors = new CellProcessor[] {
                new NotNull(new ParseLong()), // CaseId
                new NotNull(), // Timestamp
                new NotNull(), // Activity
                new NotNull(), // Resource
                new NotNull(), // State
                new Optional() // MessageType
        };

        return processors;
    }

    private void createPlace(Document document, Element net) {
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
}
