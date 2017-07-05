package at.fhjoanneum.ippr.controller;

import at.fhjoanneum.ippr.EventLoggerApplication;
import at.fhjoanneum.ippr.commons.dto.processengine.EventLoggerDTO;
import at.fhjoanneum.ippr.persistence.EventLogEntry;
import at.fhjoanneum.ippr.persistence.EventLogRepository;
import at.fhjoanneum.ippr.pmstorage.services.EventLogService;
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

    @RequestMapping(value = "newevent",  method = RequestMethod.POST)
    public String newEvent(@RequestBody final EventLoggerDTO eventLoggerDTO) {
        final Long caseId = eventLoggerDTO.getCaseId();
        final Long processModelId = eventLoggerDTO.getProcessModelId();
        final String timestamp = eventLoggerDTO.getTimestamp();
        final String activity = eventLoggerDTO.getActivity();
        final String resource = eventLoggerDTO.getResource();
        final String state = eventLoggerDTO.getState();
        final String messageType = eventLoggerDTO.getMessageType();

        final EventLogEntry eventLogEntry = new EventLogEntry(caseId, processModelId, timestamp, activity, resource, state, messageType);
        eventLogRepository.save(eventLogEntry);
        return eventLogEntry.toString();
    }

    @RequestMapping(value = "eventlog/{processModelId}", method = RequestMethod.GET)
    public @ResponseBody
    Callable<List<EventLoggerDTO>> getEventLogForCase(final HttpServletRequest request,
                                                      @PathVariable("processModelId") final int processModelId) {
        return () -> eventLogService.getEventLogForProcessModel(processModelId).get();
    }

    @RequestMapping(value = "eventlogCSV/{processModelId}", method = RequestMethod.GET)
    public @ResponseBody
    void getEventLogForCase(final HttpServletRequest request,
                                                      @PathVariable("processModelId") final int processModelId,
                                                      final HttpServletResponse response) throws IOException {
        List<EventLoggerDTO> events;

        try {
            events = eventLogService.getEventLogForProcessModel(processModelId).get();
            this.downloadCSV(response, events);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "manipulatePNML", method = RequestMethod.POST)
    public @ResponseBody void manipulatePNML(
            @RequestBody final Map<String, String> fileContents, final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
        String pnmlContent = fileContents.get("pnmlContent");
        String csvLog = fileContents.get("csvLog");
        StreamResult result = eventLogService.manipulatePNML(pnmlContent, csvLog);
        downloadPNML(response, result);
    }

    private void downloadPNML(HttpServletResponse response, StreamResult result) throws IOException {
        String pnmlFileName = "Eventlog.pnml";
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                pnmlFileName);

        response.setContentType("application/xml");
        response.setHeader(headerKey, headerValue);

        byte[] res = result.getWriter().toString().getBytes(Charset.forName("UTF-8"));

        response.setCharacterEncoding("UTF-8");
        response.getOutputStream().write(res);
        response.flushBuffer();
    }

    private void downloadCSV(HttpServletResponse response, List<EventLoggerDTO> events) throws IOException {
        String date = DateTime.now().toString("ddMMyyyy-HHmm");
        String csvFileName = "Eventlog_"+ date +".csv";

        response.setContentType("text/csv");

        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);

        // uses the Super CSV API to generate CSV data from the model data
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

        String[] header = {"CaseId", "Timestamp", "Activity", "Resource", "State",
                "MessageType"};

        csvWriter.writeHeader(header);
        events.forEach(event -> {
            try {
                if(!event.getResource().isEmpty()){
                    csvWriter.write(event, header);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        csvWriter.close();
    }
}
