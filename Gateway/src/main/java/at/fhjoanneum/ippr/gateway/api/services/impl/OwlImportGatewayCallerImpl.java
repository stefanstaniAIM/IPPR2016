package at.fhjoanneum.ippr.gateway.api.services.impl;

import java.net.URISyntaxException;
import java.util.concurrent.Future;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import at.fhjoanneum.ippr.commons.dto.owlimport.OWLProcessModelDTO;
import at.fhjoanneum.ippr.gateway.api.config.GatewayConfig;
import at.fhjoanneum.ippr.gateway.api.services.Caller;

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class OwlImportGatewayCallerImpl implements Caller {

  @Autowired
  private GatewayConfig gatewayConfig;

  @Async
  public Future<ResponseEntity<OWLProcessModelDTO>> getOWLProcessModel() throws URISyntaxException {
    final URIBuilder uri =
        new URIBuilder(gatewayConfig.getProcessModelStorageAddress()).setPath("/owlprocessmodel");

    return createRequest(uri, HttpMethod.GET, null, OWLProcessModelDTO.class, null);
  }
}
