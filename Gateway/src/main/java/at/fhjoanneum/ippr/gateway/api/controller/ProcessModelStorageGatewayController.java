package at.fhjoanneum.ippr.gateway.api.controller;

import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import at.fhjoanneum.ippr.commons.dto.pmstorage.ProcessModelDTO;
import at.fhjoanneum.ippr.gateway.api.services.ProcessModelStorageCallerImpl;

@RestController
public class ProcessModelStorageGatewayController {

  @Autowired
  private ProcessModelStorageCallerImpl processModelStorageCaller;

  @RequestMapping(name = "processes", method = RequestMethod.GET,
      produces = "application/json; charset=UTF-8")
  public @ResponseBody Callable<ResponseEntity<ProcessModelDTO[]>> findActiveProcesses(
      @RequestParam(value = "page", required = true) final int page,
      @RequestParam(value = "size", required = false, defaultValue = "10") final int size) {
    return () -> {
      return processModelStorageCaller.findActiveProcesses(page, size).get();
    };
  }
}
