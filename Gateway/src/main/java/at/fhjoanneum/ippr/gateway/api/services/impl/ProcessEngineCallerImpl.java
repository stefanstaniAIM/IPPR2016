package at.fhjoanneum.ippr.gateway.api.services.impl;

import java.net.URISyntaxException;
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
import org.springframework.web.bind.annotation.RequestBody;

import at.fhjoanneum.ippr.commons.dto.processengine.ProcessStartDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.ProcessStartedDTO;
import at.fhjoanneum.ippr.gateway.api.config.GatewayConfig;
import at.fhjoanneum.ippr.gateway.api.controller.user.HttpHeaderUser;
import at.fhjoanneum.ippr.gateway.api.services.Caller;

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ProcessEngineCallerImpl implements Caller {
  private static final Logger LOG = LoggerFactory.getLogger(ProcessEngineCallerImpl.class);

  @Autowired
  private GatewayConfig gatewayConfig;

  @Async
  public Future<ResponseEntity<ProcessStartedDTO>> startProcess(
      @RequestBody final ProcessStartDTO processStartDTO, final HttpHeaderUser headerUser)
      throws URISyntaxException {
    LOG.debug("Create request to start process instance");

    final URIBuilder uri =
        new URIBuilder(gatewayConfig.getProcessEngineAddress()).setPath("/startProcess");

    final HttpHeaders header = headerUser.getHttpHeaders();
    return createRequest(uri, HttpMethod.POST, processStartDTO, ProcessStartedDTO.class, header);
  }

  @Async
  public Future<ResponseEntity<Long>> getAmountOfActiveProcesses() throws URISyntaxException {
    final URIBuilder uri =
        new URIBuilder(gatewayConfig.getProcessEngineAddress()).setPath("/amountOfActiveProcesses");

    return createRequest(uri, HttpMethod.GET, null, Long.class, null);
  }
}
