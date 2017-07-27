package at.fhjoanneum.ippr.eventlogger.services;

import java.util.List;
import java.util.concurrent.Future;

import javax.xml.transform.stream.StreamResult;

import at.fhjoanneum.ippr.commons.dto.eventlogger.EventLoggerDTO;

public interface EventLogService {

  Future<List<EventLoggerDTO>> getEventLogForProcessModelAndSubject(final int processModelId,
      final String subject);

  StreamResult manipulatePNML(final String pnmlContent, final String csvLog) throws Exception;
}
