package at.fhjoanneum.ippr.eventlogger.controller;

import at.fhjoanneum.ippr.commons.dto.eventlogger.EventLoggerDTO;
import at.fhjoanneum.ippr.eventlogger.EventLoggerApplication;
import at.fhjoanneum.ippr.eventlogger.helper.GenerateOWLPostBodyHelper;
import at.fhjoanneum.ippr.eventlogger.persistence.EventLogEntry;
import at.fhjoanneum.ippr.eventlogger.persistence.EventLogRepository;
import at.fhjoanneum.ippr.eventlogger.services.EventLogService;
import at.fhjoanneum.ippr.eventlogger.services.GenerateOWLService;
import at.fhjoanneum.ippr.eventlogger.services.ManipulatePNMLService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

@RestController
public class EventLoggerController {

  private final static Logger LOG = LoggerFactory.getLogger(EventLoggerApplication.class);

  @Autowired
  private EventLogRepository eventLogRepository;

  @Autowired
  private EventLogService eventLogService;

  @Autowired
  private ManipulatePNMLService manipulatePNMLService;

  @Autowired
  private GenerateOWLService generateOWLService;

  @RequestMapping(value = "newevent", method = RequestMethod.POST)
  public String newEvent(@RequestBody final EventLoggerDTO eventLoggerDTO) {
    final Long caseId = eventLoggerDTO.getCaseId();
    final Long processModelId = eventLoggerDTO.getProcessModelId();
    final String timestamp = eventLoggerDTO.getTimestamp();
    final String activity = eventLoggerDTO.getActivity();
    final String resource = eventLoggerDTO.getResource();
    final String state = eventLoggerDTO.getState();
    final String messageType = eventLoggerDTO.getMessageType();
    final String recipient = eventLoggerDTO.getRecipient();
    final String sender = eventLoggerDTO.getSender();

    final EventLogEntry eventLogEntry = new EventLogEntry(caseId, processModelId, timestamp,
        activity, resource, state, messageType, recipient, sender);
    eventLogRepository.save(eventLogEntry);
    return eventLogEntry.toString();
  }

  @RequestMapping(value = "eventlog/{processModelId}/{subject}", method = RequestMethod.GET)
  public @ResponseBody Callable<List<EventLoggerDTO>> getEventLogForProcessModelAndSubject(
      final HttpServletRequest request, @PathVariable("processModelId") final int processModelId,
      @PathVariable("subject") final String subject) {
    return () -> eventLogService.getEventLogForProcessModelAndSubject(processModelId, subject)
        .get();
  }

  @RequestMapping(value = "eventlogCSV/{processModelId}/{subject}", method = RequestMethod.GET)
  public @ResponseBody void getEventLogCSV(final HttpServletRequest request,
      @PathVariable("processModelId") final int processModelId,
      @PathVariable("subject") final String subject, final HttpServletResponse response)
      throws IOException {
    List<EventLoggerDTO> events;

    try {
      events = eventLogService.getEventLogForProcessModelAndSubject(processModelId, subject).get();
      this.downloadCSV(response, events, processModelId, subject);
    } catch (final Exception e) {
      e.printStackTrace();
    }

  }

  @RequestMapping(value = "manipulatePNML", method = RequestMethod.POST)
  public @ResponseBody void manipulatePNML(@RequestBody final Map<String, String> fileContents,
      final HttpServletRequest request, final HttpServletResponse response) throws IOException {
    final String pnmlContent = fileContents.get("pnmlContent");
    final String csvLog = fileContents.get("csvLog");
    try {
      final StreamResult result = manipulatePNMLService.manipulatePNML(pnmlContent, csvLog);
      downloadXML(response, result);
    } catch (final Exception e) {
      response.sendError(400, e.getMessage());
    }
  }

  @RequestMapping(value = "generateOWL", method = RequestMethod.POST)
  public @ResponseBody void generateOWL(@RequestBody final GenerateOWLPostBodyHelper requestBody,
                                        final HttpServletRequest request, final HttpServletResponse response) throws IOException {
    try {
      final StreamResult result = generateOWLService.generateOWL(requestBody.getProcessModelName(), requestBody.getPnmlFiles());
      downloadXML(response, result);
    } catch (final Exception e) {
      response.sendError(400, e.getMessage());
    }
  }

  private void downloadXML(final HttpServletResponse response, final StreamResult result)
          throws IOException {
    response.setContentType("application/xml");

    final byte[] res = result.getWriter().toString().getBytes(Charset.forName("UTF-8"));

    response.setCharacterEncoding("UTF-8");
    response.getOutputStream().write(res);
    response.flushBuffer();
  }

  private void downloadCSV(final HttpServletResponse response, final List<EventLoggerDTO> events,
      final int processModelId, final String subject) throws IOException {
    final String date = DateTime.now().toString("ddMMyyyy-HHmm");
    final String csvFileName = "Eventlog_" + processModelId + "_" + subject + "_" + date + ".csv";

    response.setContentType("text/csv");

    final String headerKey = "Content-Disposition";
    final String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);
    response.setHeader(headerKey, headerValue);

    // uses the Super CSV API to generate CSV data from the model data
    final ICsvBeanWriter csvWriter =
        new CsvBeanWriter(response.getWriter(), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

    final String[] header =
        {"EventId", "CaseId", "Timestamp", "Activity", "Resource", "State", "MessageType", "Recipient", "Sender"};

    csvWriter.writeHeader(header);
    events.forEach(event -> {
      try {
        if (!event.getResource().isEmpty()) {
          csvWriter.write(event, header);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    });

    csvWriter.close();
  }
}
