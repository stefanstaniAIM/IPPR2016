package at.fhjoanneum.ippr.pmstorage.controller;

import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import at.fhjoanneum.ippr.commons.dto.owlimport.jsonimport.ImportProcessModelDTO;
import at.fhjoanneum.ippr.commons.dto.owlimport.reader.OWLProcessModelDTO;
import at.fhjoanneum.ippr.pmstorage.services.OwlImportService;

@RestController
@RequestMapping(produces = "application/json; charset=UTF-8")
public class OwlImportController {

  @Autowired
  private OwlImportService owlImportService;

  @RequestMapping(value = "owlprocessmodel", method = RequestMethod.POST)
  public @ResponseBody Callable<OWLProcessModelDTO> getOwlProcessModel(
      @RequestBody final String owlContent, final HttpServletRequest request) {
    return () -> {
      return owlImportService.getOwlProcessModelDTO(owlContent).get();
    };
  }

  @RequestMapping(value = "import", method = RequestMethod.POST)
  public @ResponseBody Callable<Boolean> importProcess(
      final @RequestBody ImportProcessModelDTO processModelDTO) {
    return () -> {
      return owlImportService.importProcessModel(processModelDTO).get();
    };
  }
}
