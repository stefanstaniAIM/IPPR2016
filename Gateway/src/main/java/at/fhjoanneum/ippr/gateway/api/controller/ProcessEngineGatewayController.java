package at.fhjoanneum.ippr.gateway.api.controller;

import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import at.fhjoanneum.ippr.commons.dto.processengine.ProcessStartDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.ProcessStartedDTO;
import at.fhjoanneum.ippr.gateway.api.controller.user.HttpHeaderUser;
import at.fhjoanneum.ippr.gateway.api.services.impl.ProcessEngineCallerImpl;

@RestController
@RequestMapping(produces = "application/json; charset=UTF-8")
public class ProcessEngineGatewayController {

  private static final Logger LOG =
      LoggerFactory.getLogger(ProcessModelStorageGatewayController.class);

  @Autowired
  private ProcessEngineCallerImpl processEngineCaller;

  @RequestMapping(value = "api/startProcess", method = RequestMethod.POST)
  public @ResponseBody Callable<ResponseEntity<ProcessStartedDTO>> startProcess(
      @RequestBody final ProcessStartDTO processStartDTO, final HttpServletRequest request) {

    return () -> {
      final HttpHeaderUser user = new HttpHeaderUser(request);
      return processEngineCaller.startProcess(processStartDTO, user).get();
    };
  }

  @RequestMapping(value = "api/amountOfActiveProcesses", method = RequestMethod.GET)
  public @ResponseBody Callable<ResponseEntity<Long>> getAmountOfActiveProcesses(
      final HttpServletRequest request) {

    return () -> {
      return processEngineCaller.getAmountOfActiveProcesses().get();
    };
  }

  @RequestMapping(value = "api/amountOfActiveProcessesPerUser/{userId}", method = RequestMethod.GET)
  public @ResponseBody Callable<ResponseEntity<Long>> getAmountOfActiveProcesses(
      final HttpServletRequest request, @PathVariable("userId") final Long userId) {

    return () -> {
      return processEngineCaller.getAmountOfActiveProcessesPerUser(userId).get();
    };
  }
}
