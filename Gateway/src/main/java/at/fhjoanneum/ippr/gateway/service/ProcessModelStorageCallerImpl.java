package at.fhjoanneum.ippr.gateway.service;

import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProcessModelStorageCallerImpl {

  private final Logger LOG = LoggerFactory.getLogger(ProcessModelStorageCallerImpl.class);


  private final RestTemplate restTemplate;

  public ProcessModelStorageCallerImpl(final RestTemplateBuilder restTemplateBuilder) {
    this.restTemplate = restTemplateBuilder.build();
  }

  @Async
  public Future<String> test() throws InterruptedException {
    final String url = "http://localhost:12000/test";
    final String results = restTemplate.getForObject(url, String.class);
    return new AsyncResult<>(results);
  }
}
