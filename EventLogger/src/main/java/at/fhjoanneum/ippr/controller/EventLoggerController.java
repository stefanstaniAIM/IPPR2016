package at.fhjoanneum.ippr.controller;

import at.fhjoanneum.ippr.EventLoggerApplication;
import at.fhjoanneum.ippr.commons.dto.processengine.EventLoggerDTO;
import at.fhjoanneum.ippr.persistence.EventLog;
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
import java.io.IOException;
import java.util.List;
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

        final EventLog eventLog = new EventLog(caseId, processModelId, timestamp, activity, resource, state, messageType);
        eventLogRepository.save(eventLog);
        return eventLog.toString();
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
