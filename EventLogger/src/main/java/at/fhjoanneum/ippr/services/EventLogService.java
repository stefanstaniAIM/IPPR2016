package at.fhjoanneum.ippr.pmstorage.services;

import at.fhjoanneum.ippr.commons.dto.processengine.EventLoggerDTO;

import javax.xml.transform.stream.StreamResult;
import java.util.List;
import java.util.concurrent.Future;

public interface EventLogService {

    Future<List<EventLoggerDTO>> getEventLogForProcessModel(final int processModelId);
    StreamResult manipulatePNML(final String pnmlContent, final String csvLog);
}
