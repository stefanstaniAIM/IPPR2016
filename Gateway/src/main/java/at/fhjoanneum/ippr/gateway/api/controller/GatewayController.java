package at.fhjoanneum.ippr.gateway.api.controller;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import at.fhjoanneum.ippr.gateway.api.services.ProcessModelStorageCallerImpl;

@RestController
public class GatewayController {

  private static final Logger LOG = LoggerFactory.getLogger(GatewayController.class);

  @Autowired
  private ProcessModelStorageCallerImpl processModelStorageCaller;

  @RequestMapping(value = "hello", method = RequestMethod.GET)
  public String hello() {
    return "hello from gateway :)";
  }

  @RequestMapping(value = "async", method = RequestMethod.GET)
  public @ResponseBody Callable<ResponseEntity<String>> test() {
    LOG.debug("Receive request");
    return () -> {
      LOG.debug("Return response");
      return processModelStorageCaller.test().get();
    };
  }
}
