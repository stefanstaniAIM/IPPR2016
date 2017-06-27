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
}
