package at.fhjoanneum.ippr.pmstorage.services;

import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

@Service
public class ProcessModelStorageServiceImpl {

  private static final Logger LOG = LoggerFactory.getLogger(ProcessModelStorageServiceImpl.class);

  @Async
  public Future<String> test() {
    LOG.debug("Return result");
    return new AsyncResult<String>("hello from the async gateway :)");
  }
}
