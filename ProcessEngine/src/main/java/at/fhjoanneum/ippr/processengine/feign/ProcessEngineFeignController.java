package at.fhjoanneum.ippr.processengine.feign;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import at.fhjoanneum.ippr.commons.dto.communicator.ExternalCommunicatorMessage;

@RestController
public class ProcessEngineFeignController {

  private final static Logger LOG = LoggerFactory.getLogger(ProcessEngineFeignController.class);

  @Autowired
  private ProcessEngineFeignService processEngineFeignService;

  @RequestMapping(value = "markAsSent/{transferId}")
  public void markAsSent(@PathVariable("transferId") final String transferId) {
    processEngineFeignService.markAsSent(transferId);
  }

  @RequestMapping(value = "receive", method = RequestMethod.POST)
  public void receive(@RequestBody final ExternalCommunicatorMessage msg) {
    LOG.debug("Received [{}]", msg);
    processEngineFeignService.storeExternalCommunicatorMessage(msg);
  }
}
