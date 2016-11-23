package at.fhjoanneum.ippr.processengine.controller;

import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import at.fhjoanneum.ippr.commons.dto.processengine.ProcessStartDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.ProcessStartedDTO;
import at.fhjoanneum.ippr.processengine.services.ProcessService;

@RestController
@RequestMapping(produces = "application/json; charset=UTF-8")
public class ProcessEngineController {

  private final static Logger LOG = LoggerFactory.getLogger(ProcessEngineController.class);

  @Autowired
  private ProcessService processService;

  @RequestMapping(value = "startProcess", method = RequestMethod.POST)
  public @ResponseBody Callable<ResponseEntity<ProcessStartedDTO>> startProcess(
      final HttpServletRequest request, @RequestBody final ProcessStartDTO processStartDTO) {
    LOG.info("Received request to start process with PM_ID [{}]", processStartDTO.getPmId());

    return () -> {
      final ProcessStartedDTO processStartedDTO =
          processService.startProcess(processStartDTO).get();
      if (StringUtils.isBlank(processStartedDTO.getError())) {
        return new ResponseEntity<>(processStartedDTO, HttpStatus.OK);
      } else {
        return new ResponseEntity<>(processStartedDTO, HttpStatus.BAD_REQUEST);
      }

    };
  }

  @RequestMapping(value = "amountOfActiveProcesses", method = RequestMethod.GET)
  public @ResponseBody Callable<Long> getAmountOfActiveProcesses(final HttpServletRequest request) {
    LOG.info("Received request to return the amount of processes");

    return () -> {
      return processService.getAmountOfActiveProcesses().get();
    };
  }
}
