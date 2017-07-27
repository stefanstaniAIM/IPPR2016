package at.fhjoanneum.ippr.processengine.services;

import at.fhjoanneum.ippr.commons.dto.eventlogger.EventLoggerDTO;

public interface EventLoggerSender {

  public String send(EventLoggerDTO event);
}
