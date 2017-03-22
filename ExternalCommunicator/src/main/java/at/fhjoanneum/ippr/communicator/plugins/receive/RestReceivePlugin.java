package at.fhjoanneum.ippr.communicator.plugins.receive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import at.fhjoanneum.ippr.communicator.services.ExternalCommunicatorService;

@RestController
public class RestReceivePlugin {

  private final static Logger LOG = LoggerFactory.getLogger(RestReceivePlugin.class);

  @Autowired
  private ExternalCommunicatorService externalCommunicatorService;

  @RequestMapping(value = "${rest.inbound.json}/{endpoint}", method = RequestMethod.POST,
      consumes = "application/json")
  public void post(@PathVariable final String endpoint, @RequestBody final String body) {
    externalCommunicatorService.handleExternalInputMessage(body, endpoint);
  }
}
