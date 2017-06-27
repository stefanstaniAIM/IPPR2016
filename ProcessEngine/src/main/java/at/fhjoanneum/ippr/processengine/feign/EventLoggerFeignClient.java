package at.fhjoanneum.ippr.processengine.feign;

import at.fhjoanneum.ippr.commons.dto.processengine.EventLoggerDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("event-logger")
public interface EventLoggerFeignClient {

  @RequestMapping(method = RequestMethod.POST, value = "newevent")
  public String newEvent(EventLoggerDTO event);
}
