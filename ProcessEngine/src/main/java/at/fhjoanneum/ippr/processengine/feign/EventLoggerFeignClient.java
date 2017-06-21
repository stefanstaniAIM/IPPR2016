package at.fhjoanneum.ippr.processengine.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("event-logger")
public interface EventLoggerFeignClient {

  @RequestMapping(method = RequestMethod.GET, value = "message")
  public String serviceInstancesByApplicationName();
}
