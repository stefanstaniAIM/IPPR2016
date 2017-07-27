package at.fhjoanneum.ippr.processengine.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import at.fhjoanneum.ippr.commons.dto.eventlogger.EventLoggerDTO;

@FeignClient("event-logger")
public interface EventLoggerFeignClient {

  @RequestMapping(method = RequestMethod.POST, value = "newevent")
  public String newEvent(EventLoggerDTO event);
}
