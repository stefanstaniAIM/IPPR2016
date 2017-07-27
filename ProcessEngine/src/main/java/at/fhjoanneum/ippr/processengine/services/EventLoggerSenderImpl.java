package at.fhjoanneum.ippr.processengine.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import at.fhjoanneum.ippr.commons.dto.eventlogger.EventLoggerDTO;
import at.fhjoanneum.ippr.processengine.feign.EventLoggerFeignClient;

@Service
public class EventLoggerSenderImpl implements EventLoggerSender {

  @Value("${event.logger.send}")
  private boolean sendEventLog;

  @Autowired
  private EventLoggerFeignClient client;

  @Override
  public String send(final EventLoggerDTO event) {
    if (sendEventLog) {
      return client.newEvent(event);
    }
    return null;
  }

}
