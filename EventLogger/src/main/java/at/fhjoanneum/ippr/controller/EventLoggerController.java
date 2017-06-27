package at.fhjoanneum.ippr.controller;

import at.fhjoanneum.ippr.EventLoggerApplication;
import at.fhjoanneum.ippr.commons.dto.processengine.EventLoggerDTO;
import at.fhjoanneum.ippr.persistence.EventLog;
import at.fhjoanneum.ippr.persistence.EventLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventLoggerController {

    private final static Logger LOG = LoggerFactory.getLogger(EventLoggerApplication.class);

    @Autowired
    private EventLogRepository eventLogRepository;

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
}
