package at.fhjoanneum.ippr.pmstorage.controller;

import java.util.List;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import at.fhjoanneum.ippr.commons.dto.pmstorage.FieldPermissionDTO;
import at.fhjoanneum.ippr.commons.dto.pmstorage.FieldTypeDTO;
import at.fhjoanneum.ippr.commons.dto.pmstorage.ProcessModelDTO;
import at.fhjoanneum.ippr.pmstorage.services.ProcessModelService;

@RestController
@RequestMapping(produces = "application/json; charset=UTF-8")
public class ProcessModelStorageController {

  private static final Logger LOG = LoggerFactory.getLogger(ProcessModelStorageController.class);

  @Autowired
  private ProcessModelService processModelService;

  @RequestMapping(value = "processes", method = RequestMethod.GET)
  public @ResponseBody Callable<List<ProcessModelDTO>> getAllProcesses(
      final HttpServletRequest request,
      @RequestParam(value = "page", required = true) final int page,
      @RequestParam(value = "size", required = false, defaultValue = "10") final int size) {
    return () -> {
      final PageRequest pageRequest =
          new PageRequest(page, size, new Sort(Sort.Direction.ASC, "name"));

      return processModelService.findActiveProcessModels(pageRequest).get();
    };
  }

  @RequestMapping(value = "processmodels", method = RequestMethod.GET)
  public @ResponseBody Callable<List<ProcessModelDTO>> getProcessModels(
      final HttpServletRequest request) {
    return () -> {
      return processModelService.findAllProcessModels().get();
    };
  }

  @RequestMapping(value = "processes/toStart", method = RequestMethod.GET)
  public @ResponseBody Callable<List<ProcessModelDTO>> getProcessesToStart(
      final HttpServletRequest request,
      @RequestParam(value = "page", required = true) final int page,
      @RequestParam(value = "size", required = false, defaultValue = "10") final int size) {
    return () -> {
      final GatewayUser gatewayUser = new GatewayUser(request);
      final PageRequest pageRequest =
          new PageRequest(page, size, new Sort(Sort.Direction.ASC, "name"));
      return processModelService.findActiveProcessModelsToStart(gatewayUser.getRules(), pageRequest)
          .get();
    };
  }

  @RequestMapping(value = "fieldtypes", method = RequestMethod.GET)
  public @ResponseBody Callable<List<FieldTypeDTO>> getFieldTypes() {
    return () -> {
      return processModelService.getFieldTypes().get();
    };
  }

  @RequestMapping(value = "permissions", method = RequestMethod.GET)
  public @ResponseBody Callable<List<FieldPermissionDTO>> getPermissions() {
    return () -> {
      return processModelService.getPermissions().get();
    };
  }

  @RequestMapping(value = "processes/disable/{pmId}", method = RequestMethod.POST)
  public void disableProcessModel(@PathVariable("pmId") final Long pmId) {
    final Runnable runnable = () -> {
      processModelService.disableProcessModel(pmId);
    };
    runnable.run();
  }
}
