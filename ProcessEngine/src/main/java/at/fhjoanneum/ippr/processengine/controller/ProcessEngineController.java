package at.fhjoanneum.ippr.processengine.controller;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import at.fhjoanneum.ippr.commons.dto.processengine.ProcessStartDTO;
import at.fhjoanneum.ippr.processengine.akka.messages.process.ProcessStartedMessage;
import at.fhjoanneum.ippr.processengine.dto.ProcessDTO;
import at.fhjoanneum.ippr.processengine.services.ProcessService;

@RestController
public class ProcessEngineController {

  private final static Logger LOG = LoggerFactory.getLogger(ProcessEngineController.class);

  @Autowired
  private ProcessService processService;


  public @ResponseBody Callable<ProcessDTO> startProcess(
      @PathVariable("processId") final Long processId) {
    LOG.info("Received request to start process with ID [{}]", processId);

    return () -> {
      final ProcessStartedMessage msg =
          (ProcessStartedMessage) processService.startProcess(processId).get();
      LOG.info("Return the started process");
      final ProcessDTO processDto = new ProcessDTO(msg.getProcessId());
      return processDto;
    };
  }

  @RequestMapping(value = "startProcess", method = RequestMethod.POST,
      produces = "application/json; charset=UTF-8")
  public @ResponseBody Callable<String> startProcess(
      @RequestBody final ProcessStartDTO processStartDTO) {
    LOG.info("Received request to start process with PM_ID [{}]", processStartDTO.getPmId());

    return () -> {
      processService.startProcess(processStartDTO.getPmId());
      return "yes";
    };
  }
}
