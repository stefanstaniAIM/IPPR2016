package at.fhjoanneum.ippr.communicator.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import at.fhjoanneum.ippr.commons.dto.communicator.ExternalOutputMessage;

@FeignClient("process-engine")
public interface ProcessEngineClient {

  @RequestMapping(value = "pe/markAsSent/{transferId}")
  void markAsSent(@PathVariable("transferId") final String transferId);

  @RequestMapping(value = "pe/receive", method = RequestMethod.POST)
  void notify(@RequestBody ExternalOutputMessage msg);
}
