package at.fhjoanneum.ippr.gateway.api.controller;

import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import at.fhjoanneum.ippr.commons.dto.processengine.ProcessInfoDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.ProcessStartDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.ProcessStartedDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.ProcessStateDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.TaskDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.stateobject.StateObjectChangeDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.stateobject.StateObjectDTO;
import at.fhjoanneum.ippr.gateway.api.controller.user.HttpHeaderUser;
import at.fhjoanneum.ippr.gateway.api.services.impl.ProcessEngineCallerImpl;

@RestController
@RequestMapping(produces = "application/json; charset=UTF-8")
public class ProcessEngineGatewayController {

  private static final Logger LOG =
      LoggerFactory.getLogger(ProcessModelStorageGatewayController.class);

  @Autowired
  private ProcessEngineCallerImpl processEngineCaller;

  @RequestMapping(value = "api/processes/startProcess", method = RequestMethod.POST)
  public @ResponseBody Callable<ResponseEntity<ProcessStartedDTO>> startProcess(
      @RequestBody final ProcessStartDTO processStartDTO, final HttpServletRequest request) {

    return () -> {
      final HttpHeaderUser user = new HttpHeaderUser(request);
      return processEngineCaller.startProcess(processStartDTO, user).get();
    };
  }

  @RequestMapping(value = "api/processes/count/{state}", method = RequestMethod.GET)
  public @ResponseBody Callable<ResponseEntity<Long>> getAmountOfProcesses(
      final HttpServletRequest request, @PathVariable("state") final String state) {

    return () -> {
      return processEngineCaller.getAmountOfProcessesInState(state).get();
    };
  }

  @RequestMapping(value = "api/processes/count/{state}/{userId}", method = RequestMethod.GET)
  public @ResponseBody Callable<ResponseEntity<Long>> getAmountOfProcesses(
      final HttpServletRequest request, @PathVariable("state") final String state,
      @PathVariable("userId") final Long userId) {

    return () -> {
      return processEngineCaller.getAmountOfProcessesInStatePerUser(state, userId).get();
    };
  }

  @RequestMapping(value = "api/processes/count/started/{hoursbefore}", method = RequestMethod.GET)
  public @ResponseBody Callable<ResponseEntity<Long>> getAmountOfStartedProcessesInRange(
      final HttpServletRequest request, @PathVariable("hoursbefore") final Long hoursbefore) {

    return () -> {
      return processEngineCaller.getAmountOfStartedProcessesInRange(hoursbefore).get();
    };
  }

  @RequestMapping(value = "api/processes/count/finished/{hoursbefore}", method = RequestMethod.GET)
  public @ResponseBody Callable<ResponseEntity<Long>> getAmountOfFinishedProcessesInRange(
      final HttpServletRequest request, @PathVariable("hoursbefore") final Long hoursbefore) {

    return () -> {
      return processEngineCaller.getAmountOfFinishedProcessesInRange(hoursbefore).get();
    };
  }

  @RequestMapping(value = "api/processes/count/started/{hoursbefore}/{userId}",
      method = RequestMethod.GET)
  public @ResponseBody Callable<ResponseEntity<Long>> getAmountOfStartedProcessesInRangeForUser(
      final HttpServletRequest request, @PathVariable("hoursbefore") final Long hoursbefore,
      @PathVariable("userId") final Long userId) {

    return () -> {
      return processEngineCaller.getAmountOfStartedProcessesForUserInRange(hoursbefore, userId)
          .get();
    };
  }

  @RequestMapping(value = "api/processes/count/finished/{hoursbefore}/{userId}",
      method = RequestMethod.GET)
  public @ResponseBody Callable<ResponseEntity<Long>> getAmountOfFinishedProcessesInRangeForUser(
      final HttpServletRequest request, @PathVariable("hoursbefore") final Long hoursbefore,
      @PathVariable("userId") final Long userId) {

    return () -> {
      return processEngineCaller.getAmountOfFinishedProcessesForUserInRange(hoursbefore, userId)
          .get();
    };
  }

  @RequestMapping(value = "api/processes/state/{piId}", method = RequestMethod.GET)
  public @ResponseBody Callable<ProcessStateDTO> getProcessState(final HttpServletRequest request,
      @PathVariable("piId") final Long piId) {

    return () -> {
      return processEngineCaller.getProcessState(piId).get();
    };
  }

  @RequestMapping(value = "api/processes/{state}", method = RequestMethod.GET)
  public @ResponseBody Callable<List<ProcessInfoDTO>> getProcessesInfoOfState(
      final HttpServletRequest request, @PathVariable("state") final String state,
      @RequestParam(value = "page", required = true) final int page,
      @RequestParam(value = "size", required = false, defaultValue = "10") final int size) {

    return () -> {
      return processEngineCaller.getProcessesInfoOfState(state, page, size).get();
    };
  }

  @RequestMapping(value = "api/processes/{state}/{user}", method = RequestMethod.GET)
  public @ResponseBody Callable<List<ProcessInfoDTO>> getProcessesInfoOfState(
      final HttpServletRequest request, @PathVariable("user") final Long user,
      @PathVariable("state") final String state,
      @RequestParam(value = "page", required = true) final int page,
      @RequestParam(value = "size", required = false, defaultValue = "10") final int size) {

    return () -> {
      return processEngineCaller.getProcessesInfoOfUserAndState(user, state, page, size).get();
    };
  }

  @RequestMapping(value = "api/processes/stop/{piId}", method = RequestMethod.POST)
  public Callable<ResponseEntity<ProcessInfoDTO>> stopProcessInstance(
      final HttpServletRequest request, @PathVariable("piId") final Long piId) {
    LOG.debug("Received request to stop process PI_ID [{}]", piId);

    return () -> {
      final HttpHeaderUser user = new HttpHeaderUser(request);
      return processEngineCaller.stopProcess(user, piId).get();
    };
  }

  @RequestMapping(value = "api/processes/tasks/{userId}", method = RequestMethod.GET)
  public Callable<ResponseEntity<TaskDTO[]>> getTasksOfUser(final HttpServletRequest request,
      @PathVariable("userId") final Long userId) {
    LOG.debug("Received request to return tasks of user [{}]", userId);

    return () -> {
      final HttpHeaderUser headerUser = new HttpHeaderUser(request);
      return processEngineCaller.getTasksOfUser(headerUser, userId).get();
    };
  }

  @RequestMapping(value = "api/processes/task/{piId}/{userId}", method = RequestMethod.GET)
  public Callable<ResponseEntity<StateObjectDTO>> getStateObjectOfUserInProcess(
      final HttpServletRequest request, @PathVariable("piId") final Long piId,
      @PathVariable("userId") final Long userId) {
    return () -> {
      final HttpHeaderUser headerUser = new HttpHeaderUser(request);
      return processEngineCaller.getStateObjectOfUserInProcess(headerUser, piId, userId).get();
    };
  }

  @RequestMapping(value = "api/processes/task/{piId}/{userId}", method = RequestMethod.POST)
  public Callable<ResponseEntity<Boolean>> changeStateOfUserInProcess(
      final HttpServletRequest request, @PathVariable("piId") final Long piId,
      @PathVariable("userId") final Long userId,
      @RequestBody final StateObjectChangeDTO stateObjectChangeDTO) {
    return () -> {
      final HttpHeaderUser headerUser = new HttpHeaderUser(request);
      return processEngineCaller
          .changeStateOfUserInProcess(headerUser, piId, userId, stateObjectChangeDTO).get();
    };
  }
}
