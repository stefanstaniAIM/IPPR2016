package at.fhjoanneum.ippr.communicator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import at.fhjoanneum.ippr.commons.dto.communicator.ExternalOutputMessage;
import at.fhjoanneum.ippr.communicator.services.ExternalCommunicatorService;

@RestController
public class ExternalCommunicatorController {

  @Autowired
  private ExternalCommunicatorService externalCommunicatorService;

  @RequestMapping(method = RequestMethod.POST, value = "outputmessage")
  public void handleExternalOutputMessage(@RequestBody final ExternalOutputMessage message) {
    externalCommunicatorService.handleExternalOutputMessage(message);
  }
}
