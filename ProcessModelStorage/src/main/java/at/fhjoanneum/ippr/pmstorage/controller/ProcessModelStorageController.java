package at.fhjoanneum.ippr.pmstorage.controller;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import at.fhjoanneum.ippr.pmstorage.services.ProcessModelStorageServiceImpl;

@RestController
public class ProcessModelStorageController {

  private static final Logger LOG = LoggerFactory.getLogger(ProcessModelStorageController.class);

  @Autowired
  private ProcessModelStorageServiceImpl processModelStorageService;

  @RequestMapping(value = "test", method = RequestMethod.GET)
  public @ResponseBody Callable<String> test() {
    LOG.debug("Received request");
    return () -> {
      LOG.debug("Waiting for service response");
      return processModelStorageService.test().get();
    };
  }
}
