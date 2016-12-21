package at.fhjoanneum.ippr.gateway.api.services.impl;

import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.RequestBody;

import com.google.common.collect.Lists;

import at.fhjoanneum.ippr.commons.dto.processengine.ProcessInfoDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.ProcessStartDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.ProcessStartedDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.ProcessStateDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.StateObjectDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.TaskDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.UserContainer;
import at.fhjoanneum.ippr.commons.dto.processengine.UserDTO;
import at.fhjoanneum.ippr.gateway.api.config.GatewayConfig;
import at.fhjoanneum.ippr.gateway.api.controller.user.HttpHeaderUser;
import at.fhjoanneum.ippr.gateway.api.services.Caller;
import at.fhjoanneum.ippr.gateway.security.persistence.objects.User;
import at.fhjoanneum.ippr.gateway.security.repositories.UserGroupRepository;

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ProcessEngineCallerImpl implements Caller {
  private static final Logger LOG = LoggerFactory.getLogger(ProcessEngineCallerImpl.class);

  @Autowired
  private GatewayConfig gatewayConfig;

  @Autowired
  private UserGroupRepository userGroupRepository;

  @Async
  public Future<ResponseEntity<ProcessStartedDTO>> startProcess(
      @RequestBody final ProcessStartDTO processStartDTO, final HttpHeaderUser headerUser)
      throws URISyntaxException {
    LOG.debug("Create request to start process instance");

    final URIBuilder uri =
        new URIBuilder(gatewayConfig.getProcessEngineAddress()).setPath("/processes/startProcess");

    final HttpHeaders header = headerUser.getHttpHeaders();
    return createRequest(uri, HttpMethod.POST, processStartDTO, ProcessStartedDTO.class, header);
  }

  @Async
  public Future<ResponseEntity<Long>> getAmountOfActiveProcesses() throws URISyntaxException {
    final URIBuilder uri = new URIBuilder(gatewayConfig.getProcessEngineAddress())
        .setPath("/processes/amountOfActiveProcesses");

    return createRequest(uri, HttpMethod.GET, null, Long.class, null);
  }

  @Async
  public Future<ResponseEntity<Long>> getAmountOfActiveProcessesPerUser(final Long userId)
      throws URISyntaxException {
    final URIBuilder uri = new URIBuilder(gatewayConfig.getProcessEngineAddress())
        .setPath("/processes/amountOfActiveProcessesPerUser/" + userId);

    return createRequest(uri, HttpMethod.GET, null, Long.class, null);
  }

  @Async
  public Future<ProcessStateDTO> getProcessState(final Long piId) throws URISyntaxException {
    final CompletableFuture<ProcessStateDTO> future = new CompletableFuture<>();

    final URIBuilder uri =
        new URIBuilder(gatewayConfig.getProcessEngineAddress()).setPath("processes/state/" + piId);

    final ListenableFuture<ResponseEntity<ProcessStateDTO>> responseFuture =
        createRequest(uri, HttpMethod.GET, null, ProcessStateDTO.class, null);

    responseFuture.addCallback(result -> {
      final List<UserContainer> container = Lists.newArrayList(result.getBody().getSubjects());
      getUser(container);
      future.complete(result.getBody());
    }, null);

    return future;
  }

  @Async
  public Future<List<ProcessInfoDTO>> getProcessesInfoOfState(final String state, final int page,
      final int size) throws URISyntaxException {
    final CompletableFuture<List<ProcessInfoDTO>> future = new CompletableFuture<>();

    final URIBuilder uri =
        new URIBuilder(gatewayConfig.getProcessEngineAddress()).setPath("processes/" + state)
            .addParameter("page", String.valueOf(page)).addParameter("size", String.valueOf(size));

    final ListenableFuture<ResponseEntity<ProcessInfoDTO[]>> responseFuture =
        createRequest(uri, HttpMethod.GET, null, ProcessInfoDTO[].class, null);

    appendUserInformation(responseFuture, future);

    return future;
  }

  @Async
  public Future<List<ProcessInfoDTO>> getProcessesInfoOfUserAndState(final Long user,
      final String state, final int page, final int size) throws URISyntaxException {
    final CompletableFuture<List<ProcessInfoDTO>> future = new CompletableFuture<>();

    final URIBuilder uri = new URIBuilder(gatewayConfig.getProcessEngineAddress())
        .setPath("processes/" + state + "/" + user).addParameter("page", String.valueOf(page))
        .addParameter("size", String.valueOf(size));

    final ListenableFuture<ResponseEntity<ProcessInfoDTO[]>> responseFuture =
        createRequest(uri, HttpMethod.GET, null, ProcessInfoDTO[].class, null);

    appendUserInformation(responseFuture, future);

    return future;
  }

  @Async
  private <T extends UserContainer> void appendUserInformation(
      final ListenableFuture<ResponseEntity<T[]>> responseFuture,
      final CompletableFuture<List<T>> future) {

    responseFuture.addCallback(result -> {
      final List<T> entries = Lists.newArrayList(result.getBody());
      getUser(Lists.newArrayList(result.getBody()));
      future.complete(entries);
    }, null);
  }

  @Async
  private void getUser(final List<UserContainer> userContainer) {
    userContainer.stream().filter(entry -> entry.getUserId() != null).forEach(entry -> {
      final User user = userGroupRepository.getUserByUserId(entry.getUserId()).get();
      final UserDTO userDTO =
          new UserDTO(user.getUsername(), user.getFirstname(), user.getLastname());
      entry.appendUser(userDTO);
    });
  }

  @Async
  public Future<ResponseEntity<ProcessInfoDTO>> stopProcess(final HttpHeaderUser headerUser,
      final Long piId) throws URISyntaxException {
    final URIBuilder uri =
        new URIBuilder(gatewayConfig.getProcessEngineAddress()).setPath("/processes/stop/" + piId);

    final HttpHeaders header = headerUser.getHttpHeaders();
    return createRequest(uri, HttpMethod.POST, null, ProcessInfoDTO.class, header);
  }

  public Future<ResponseEntity<TaskDTO[]>> getTasksOfUser(final HttpHeaderUser headerUser,
      final Long userId) throws URISyntaxException {
    final URIBuilder uri = new URIBuilder(gatewayConfig.getProcessEngineAddress())
        .setPath("/processes/tasks/" + userId);

    final HttpHeaders header = headerUser.getHttpHeaders();
    return createRequest(uri, HttpMethod.GET, null, TaskDTO[].class, header);
  }

  public Future<ResponseEntity<StateObjectDTO>> getStateObjectOfUserInProcess(
      final HttpHeaderUser headerUser, final Long piId, final Long userId)
      throws URISyntaxException {
    final URIBuilder uri = new URIBuilder(gatewayConfig.getProcessEngineAddress())
        .setPath("/processes/task/" + piId + "/" + userId);

    final HttpHeaders header = headerUser.getHttpHeaders();
    return createRequest(uri, HttpMethod.GET, null, StateObjectDTO.class, header);
  }
}
