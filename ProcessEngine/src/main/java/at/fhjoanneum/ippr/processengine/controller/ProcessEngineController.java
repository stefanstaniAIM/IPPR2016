package at.fhjoanneum.ippr.processengine.controller;

import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import at.fhjoanneum.ippr.commons.dto.processengine.ProcessStartDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.ProcessStartedDTO;
import at.fhjoanneum.ippr.processengine.services.ProcessService;

@RestController
public class ProcessEngineController {

  private final static Logger LOG = LoggerFactory.getLogger(ProcessEngineController.class);

  @Autowired
  private ProcessService processService;

  @RequestMapping(value = "startProcess", method = RequestMethod.POST,
      produces = "application/json; charset=UTF-8")
  public @ResponseBody Callable<ProcessStartedDTO> startProcess(final HttpServletRequest request,
      @RequestBody final ProcessStartDTO processStartDTO) {
    LOG.info("Received request to start process with PM_ID [{}]", processStartDTO.getPmId());

    return () -> {
      return processService.startProcess(processStartDTO).get();
    };
  }
}
