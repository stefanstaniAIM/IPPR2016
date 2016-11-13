package at.fhjoanneum.ippr.gateway.api.services;

import java.net.URISyntaxException;
import java.util.concurrent.Future;

import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.AsyncRestTemplate;

import at.fhjoanneum.ippr.commons.dto.pmstorage.ProcessModelDTO;
import at.fhjoanneum.ippr.gateway.api.config.GatewayConfig;

@Service
public class ProcessModelStorageCallerImpl {

  private final Logger LOG = LoggerFactory.getLogger(ProcessModelStorageCallerImpl.class);

  @Autowired
  private GatewayConfig gatewayConfig;

  private final AsyncRestTemplate restTemplate;

  public ProcessModelStorageCallerImpl() {
    this.restTemplate = new AsyncRestTemplate();
  }

  @Async
  public Future<ResponseEntity<ProcessModelDTO[]>> findActiveProcesses(final int page,
      final int size) throws URISyntaxException {
    LOG.debug("Create request to process model storage");
    final URIBuilder uri = new URIBuilder(gatewayConfig.getProcessModelStorageAddress())
        .setPath("/processes").addParameter("page", "" + page).addParameter("size", "" + size);
    return restTemplate.getForEntity(uri.toString(), ProcessModelDTO[].class);
  }

}
