package at.fhjoanneum.ippr.pmstorage.controller;

import java.util.List;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import at.fhjoanneum.ippr.commons.dto.pmstorage.ProcessModelDTO;
import at.fhjoanneum.ippr.pmstorage.services.impl.ProcessModelServiceImpl;

@RestController
public class ProcessModelStorageController {

  private static final Logger LOG = LoggerFactory.getLogger(ProcessModelStorageController.class);

  @Autowired
  private ProcessModelServiceImpl processModelService;

  @RequestMapping(value = "processes", method = RequestMethod.GET,
      produces = "application/json; charset=UTF-8")
  public @ResponseBody Callable<List<ProcessModelDTO>> getAllProcesses(
      @RequestParam(value = "page", required = true) final int page,
      @RequestParam(value = "size", required = false, defaultValue = "10") final int size) {
    return () -> {
      LOG.debug("Received request to show all active processes");
      final PageRequest pageRequest =
          new PageRequest(page, size, new Sort(Sort.Direction.ASC, "name"));

      return processModelService.findActiveProcessModels(pageRequest).get();
    };
  }
}
