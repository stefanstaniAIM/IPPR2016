package at.fhjoanneum.ippr.gateway.api.controller;

import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;

import at.fhjoanneum.ippr.commons.dto.owlimport.OWLProcessModelDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import at.fhjoanneum.ippr.commons.dto.pmstorage.FieldTypeDTO;
import at.fhjoanneum.ippr.commons.dto.pmstorage.ProcessModelDTO;
import at.fhjoanneum.ippr.gateway.api.controller.user.HttpHeaderUser;
import at.fhjoanneum.ippr.gateway.api.services.impl.ProcessModelStorageCallerImpl;

@RestController
@RequestMapping(produces = "application/json; charset=UTF-8")
public class ProcessModelStorageGatewayController {

  private static final Logger LOG =
      LoggerFactory.getLogger(ProcessModelStorageGatewayController.class);

  @Autowired
  private ProcessModelStorageCallerImpl processModelStorageCaller;

  @RequestMapping(value = "api/processes", method = RequestMethod.GET)
  public @ResponseBody Callable<ResponseEntity<ProcessModelDTO[]>> findActiveProcesses(
      final HttpServletRequest request,
      @RequestParam(value = "page", required = true) final int page,
      @RequestParam(value = "size", required = false, defaultValue = "10") final int size) {
    return () -> {
      final HttpHeaderUser user = new HttpHeaderUser(request);
      return processModelStorageCaller.findActiveProcesses(user, page, size).get();
    };
  }

  @RequestMapping(value = "api/processes/toStart", method = RequestMethod.GET)
  public @ResponseBody Callable<ResponseEntity<ProcessModelDTO[]>> findActiveProcessesToStart(
      final HttpServletRequest request,
      @RequestParam(value = "page", required = true) final int page,
      @RequestParam(value = "size", required = false, defaultValue = "10") final int size) {
    return () -> {
      final HttpHeaderUser user = new HttpHeaderUser(request);
      return processModelStorageCaller.findActiveProcessesToStart(user, page, size).get();
    };
  }

  @RequestMapping(value = "api/fieldtypes", method = RequestMethod.GET)
  public @ResponseBody Callable<ResponseEntity<FieldTypeDTO[]>> findActiveProcessesToStart(
      final HttpServletRequest request) {
    return () -> {
      return processModelStorageCaller.getFieldTypes().get();
    };
  }

  @RequestMapping(value = "api/owlprocessmodel", method = RequestMethod.GET)
  public @ResponseBody Callable<ResponseEntity<OWLProcessModelDTO[]>> getOWLProcessModel(
          final HttpServletRequest request) {
    return () -> {
      return processModelStorageCaller.getOWLProcessModel().get();
    };
  }
}
