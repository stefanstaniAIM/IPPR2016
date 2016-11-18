package at.fhjoanneum.ippr.gateway.api.services;

import java.net.URISyntaxException;
import java.util.concurrent.Future;

import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.AsyncRestTemplate;

import at.fhjoanneum.ippr.commons.dto.pmstorage.ProcessModelDTO;
import at.fhjoanneum.ippr.gateway.api.config.GatewayConfig;
import at.fhjoanneum.ippr.gateway.api.controller.user.HttpHeaderUser;

@Service
public class ProcessModelStorageCallerImpl {

  private final Logger LOG = LoggerFactory.getLogger(ProcessModelStorageCallerImpl.class);

  @Autowired
  private GatewayConfig gatewayConfig;

  @Async
  public Future<ResponseEntity<ProcessModelDTO[]>> findActiveProcesses(
      final HttpHeaderUser httpHeaderUser, final int page, final int size)
      throws URISyntaxException {
    LOG.debug("Create request to process model storage");
    final AsyncRestTemplate restTemplate = new AsyncRestTemplate();


    final URIBuilder uri = new URIBuilder(gatewayConfig.getProcessModelStorageAddress())
        .setPath("/processes").addParameter("page", "" + page).addParameter("size", "" + size);

    final HttpEntity<String> entity = new HttpEntity<String>(null, httpHeaderUser.getHttpHeaders());
    return restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, ProcessModelDTO[].class);
  }

  @Async
  public Future<ResponseEntity<ProcessModelDTO[]>> findActiveProcessesToStart(
      final HttpHeaderUser httpHeaderUser, final int page, final int size)
      throws URISyntaxException {
    LOG.debug("Create request to process model storage");
    final AsyncRestTemplate restTemplate = new AsyncRestTemplate();


    final URIBuilder uri =
        new URIBuilder(gatewayConfig.getProcessModelStorageAddress()).setPath("/processesToStart")
            .addParameter("page", "" + page).addParameter("size", "" + size);

    final HttpEntity<String> entity = new HttpEntity<String>(null, httpHeaderUser.getHttpHeaders());
    return restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, ProcessModelDTO[].class);
  }
}
