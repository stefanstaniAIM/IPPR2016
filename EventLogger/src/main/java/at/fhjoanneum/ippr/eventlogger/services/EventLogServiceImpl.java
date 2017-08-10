package at.fhjoanneum.ippr.eventlogger.services;


import at.fhjoanneum.ippr.commons.dto.eventlogger.EventLoggerDTO;
import at.fhjoanneum.ippr.eventlogger.persistence.EventLogEntry;
import at.fhjoanneum.ippr.eventlogger.persistence.EventLogRepository;
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
  public Future<List<EventLoggerDTO>> getEventLogForProcessModelAndSubject(final int processModelId,
      final String subject) {
    final List<EventLogEntry> results =
        eventLogRepository.getEventLogForProcessModelAndSubject(processModelId, subject);
    final List<EventLoggerDTO> eventLog = createEventLoggerDTO(results);
    return new AsyncResult<List<EventLoggerDTO>>(eventLog);
  }

  private static List<EventLoggerDTO> createEventLoggerDTO(final List<EventLogEntry> results) {
    final List<EventLoggerDTO> eventLog = Lists.newArrayList();

    results.forEach(event -> {
      final EventLoggerDTO dto = new EventLoggerDTO(event.getEventId(), event.getCaseId(),
          event.getProcessModelId(), event.getTimestamp(), event.getActivity(), event.getResource(),
          event.getState(), event.getMessageType(), event.getRecipient(), event.getSender());
      eventLog.add(dto);
    });

    return eventLog;
  }
}
