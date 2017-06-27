package at.fhjoanneum.ippr.pmstorage.services;

import java.util.List;
import java.util.concurrent.Future;

import at.fhjoanneum.ippr.commons.dto.processengine.EventLoggerDTO;
import org.springframework.data.domain.Pageable;

import at.fhjoanneum.ippr.commons.dto.pmstorage.FieldPermissionDTO;
import at.fhjoanneum.ippr.commons.dto.pmstorage.FieldTypeDTO;
import at.fhjoanneum.ippr.commons.dto.pmstorage.ProcessModelDTO;

public interface EventLogService {

    Future<List<EventLoggerDTO>> getEventLogForProcessModel(final int processModelId);
}
