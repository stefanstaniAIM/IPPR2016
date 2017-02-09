package at.fhjoanneum.ippr.gateway.api.services.impl;

import java.net.URISyntaxException;
import java.util.concurrent.Future;

import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import at.fhjoanneum.ippr.commons.dto.pmstorage.FieldPermissionDTO;
import at.fhjoanneum.ippr.commons.dto.pmstorage.FieldTypeDTO;
import at.fhjoanneum.ippr.commons.dto.pmstorage.ProcessModelDTO;
import at.fhjoanneum.ippr.gateway.api.config.GatewayConfig;
import at.fhjoanneum.ippr.gateway.api.controller.user.HttpHeaderUser;
import at.fhjoanneum.ippr.gateway.api.services.Caller;

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ProcessModelStorageCallerImpl implements Caller {

  private final Logger LOG = LoggerFactory.getLogger(ProcessModelStorageCallerImpl.class);

  @Autowired
  private GatewayConfig gatewayConfig;

  @Async
  public Future<ResponseEntity<ProcessModelDTO[]>> findActiveProcesses(
      final HttpHeaderUser httpHeaderUser, final int page, final int size)
      throws URISyntaxException {
    LOG.debug("Create request to process model storage");

    final URIBuilder uri = new URIBuilder(gatewayConfig.getProcessModelStorageAddress())
        .setPath("/processes").addParameter("page", "" + page).addParameter("size", "" + size);

    return createRequest(uri, HttpMethod.GET, null, ProcessModelDTO[].class,
        httpHeaderUser.getHttpHeaders());
  }

  @Async
  public Future<ResponseEntity<ProcessModelDTO[]>> findActiveProcessesToStart(
      final HttpHeaderUser httpHeaderUser, final int page, final int size)
      throws URISyntaxException {
    LOG.debug("Create request to process model storage");

    final URIBuilder uri =
        new URIBuilder(gatewayConfig.getProcessModelStorageAddress()).setPath("/processes/toStart")
            .addParameter("page", "" + page).addParameter("size", "" + size);

    return createRequest(uri, HttpMethod.GET, null, ProcessModelDTO[].class,
        httpHeaderUser.getHttpHeaders());
  }

  @Async
  public Future<ResponseEntity<FieldTypeDTO[]>> getFieldTypes() throws URISyntaxException {
    final URIBuilder uri =
        new URIBuilder(gatewayConfig.getProcessModelStorageAddress()).setPath("/fieldtypes");

    return createRequest(uri, HttpMethod.GET, null, FieldTypeDTO[].class, null);
  }

  @Async
  public Future<ResponseEntity<FieldPermissionDTO[]>> getPermissions() throws URISyntaxException {
    final URIBuilder uri =
        new URIBuilder(gatewayConfig.getProcessModelStorageAddress()).setPath("/permissions");

    return createRequest(uri, HttpMethod.GET, null, FieldPermissionDTO[].class, null);
  }

  @Async
  public void disableProcessModel(final Long pmId) throws URISyntaxException {
    final URIBuilder uri = new URIBuilder(gatewayConfig.getProcessModelStorageAddress())
        .setPath("processes/disable/" + pmId);
    createRequest(uri, HttpMethod.POST, null, null, null);
    return;
  }

  @Async
  public Future<ResponseEntity<ProcessModelDTO[]>> findAllProcessModels()
      throws URISyntaxException {
    final URIBuilder uri =
        new URIBuilder(gatewayConfig.getProcessModelStorageAddress()).setPath("/processmodels");

    return createRequest(uri, HttpMethod.GET, null, ProcessModelDTO[].class, null);
  }
}
