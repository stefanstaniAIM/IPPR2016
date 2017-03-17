package at.fhjoanneum.ippr.communicator.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("process-engine")
public interface ProcessEngineClient {

  @RequestMapping(value = "pe/markAsSent/{transferId}")
  void markAsSent(@PathVariable("transferId") final String transferId);
}
