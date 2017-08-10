package at.fhjoanneum.ippr.eventlogger.services;

import at.fhjoanneum.ippr.commons.dto.eventlogger.EventLoggerDTO;

import java.util.List;
import java.util.concurrent.Future;

public interface EventLogService {

  Future<List<EventLoggerDTO>> getEventLogForProcessModelAndSubject(final int processModelId,
      final String subject);

}