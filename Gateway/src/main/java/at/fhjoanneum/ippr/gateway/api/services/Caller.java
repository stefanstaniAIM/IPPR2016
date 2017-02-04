package at.fhjoanneum.ippr.gateway.api.services;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.DefaultResponseErrorHandler;


public interface Caller {

  /**
   * Method to create http requests to services
   */
  public default <I, O> ListenableFuture<ResponseEntity<O>> createRequest(final URIBuilder uri,
      final HttpMethod method, final I body, final Class<O> returnClazz, final HttpHeaders header) {
    final AsyncRestTemplate restTemplate = new AsyncRestTemplate();
    restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
    if (header != null) {
      header.setContentType(MediaType.APPLICATION_JSON);
    }

    HttpEntity<?> entity;
    if (body != null) {
      entity = new HttpEntity<I>(body, header);
    } else {
      entity = new HttpEntity<String>(null, header);
    }

    return restTemplate.exchange(uri.toString(), method, entity, returnClazz);
  }
}
