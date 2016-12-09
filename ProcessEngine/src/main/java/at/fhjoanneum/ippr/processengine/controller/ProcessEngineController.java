package at.fhjoanneum.ippr.processengine.controller;

import java.util.List;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import at.fhjoanneum.ippr.commons.dto.processengine.ProcessInfoDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.ProcessStartDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.ProcessStartedDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.ProcessStateDTO;
import at.fhjoanneum.ippr.processengine.services.ProcessService;

@RestController
@RequestMapping(produces = "application/json; charset=UTF-8")
public class ProcessEngineController {

  private final static Logger LOG = LoggerFactory.getLogger(ProcessEngineController.class);

  @Autowired
  private ProcessService processService;

  @RequestMapping(value = "processes/startProcess", method = RequestMethod.POST)
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

  @RequestMapping(value = "processes/amountOfActiveProcesses", method = RequestMethod.GET)
  public @ResponseBody Callable<Long> getAmountOfActiveProcesses(final HttpServletRequest request) {
    LOG.info("Received request to return the amount of processes");

    return () -> {
      return processService.getAmountOfActiveProcesses().get();
    };
  }

  @RequestMapping(value = "processes/amountOfActiveProcessesPerUser/{userId}",
      method = RequestMethod.GET)
  public @ResponseBody Callable<Long> getAmountOfActiveProcessesPerUser(
      final HttpServletRequest request, @PathVariable("userId") final Long userId) {
    LOG.info("Received request to return the amount of processes per user for userId: {}", userId);

    return () -> {
      return processService.getAmountOfActiveProcessesPerUser(userId).get();
    };
  }

  @RequestMapping(value = "processes/state/{piId}", method = RequestMethod.GET)
  public @ResponseBody Callable<ProcessStateDTO> getStateOfProcessInstance(
      final HttpServletRequest request, @PathVariable("piId") final Long piId) {
    LOG.info("Received request to return state information of process for piId: {}", piId);

    return () -> {
      return processService.getStateOfProcessInstance(piId).get();
    };
  }

  @RequestMapping(value = "processes/{state}", method = RequestMethod.GET)
  public @ResponseBody Callable<List<ProcessInfoDTO>> getProcessesInfoOfState(
      @PathVariable("state") final String state,
      @RequestParam(value = "page", required = true) final int page,
      @RequestParam(value = "size", required = false, defaultValue = "10") final int size) {

    return () -> {
      return processService.getProcessesInfoOfState(state, page, size).get();
    };
  }


  @RequestMapping(value = "processes/{state}/{user}", method = RequestMethod.GET)
  public @ResponseBody Callable<List<ProcessInfoDTO>> getProcessesInfoOfUserAndState(
      @PathVariable("user") final Long user, @PathVariable("state") final String state,
      @RequestParam(value = "page", required = true) final int page,
      @RequestParam(value = "size", required = false, defaultValue = "10") final int size) {

    return () -> {
      return processService.getProcessesInfoOfUserAndState(user, state, page, size).get();
    };
  }


  @RequestMapping(value = "processes/stop/{piId}", method = RequestMethod.POST)
  public Callable<ProcessInfoDTO> stopProcessInstance(final HttpServletRequest request,
      @PathVariable("piId") final Long piId) {
    LOG.debug("Received request to stop process PI_ID [{}]", piId);

    return () -> {
      return processService.stopProcess(piId).get();
    };
  }
}
