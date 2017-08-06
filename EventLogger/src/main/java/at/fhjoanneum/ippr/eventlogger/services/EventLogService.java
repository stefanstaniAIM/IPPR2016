package at.fhjoanneum.ippr.eventlogger.services;

import at.fhjoanneum.ippr.commons.dto.eventlogger.EventLoggerDTO;

import javax.xml.transform.stream.StreamResult;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public interface EventLogService {

  Future<List<EventLoggerDTO>> getEventLogForProcessModelAndSubject(final int processModelId,
      final String subject);

  StreamResult manipulatePNML(final String pnmlContent, final String csvLog) throws Exception;

  StreamResult generateOWL(final String processModelName, final Map<String, String> petriNets) throws Exception;
}