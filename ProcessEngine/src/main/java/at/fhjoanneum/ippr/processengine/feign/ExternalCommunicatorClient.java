package at.fhjoanneum.ippr.processengine.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import at.fhjoanneum.ippr.commons.dto.communicator.ExternalOutputMessage;

@FeignClient("external-communicator")
public interface ExternalCommunicatorClient {

  @RequestMapping(method = RequestMethod.POST, value = "outputmessage")
  void handleExternalOutputMessage(@RequestBody final ExternalOutputMessage message);
}
