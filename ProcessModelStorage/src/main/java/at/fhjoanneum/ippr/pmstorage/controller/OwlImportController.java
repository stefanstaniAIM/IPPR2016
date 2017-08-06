package at.fhjoanneum.ippr.pmstorage.controller;

import at.fhjoanneum.ippr.commons.dto.owlimport.jsonimport.ImportProcessModelDTO;
import at.fhjoanneum.ippr.commons.dto.owlimport.reader.OWLProcessModelDTO;
import at.fhjoanneum.ippr.pmstorage.services.OwlImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.Callable;

@RestController
@RequestMapping(produces = "application/json; charset=UTF-8")
public class OwlImportController {

  @Autowired
  private OwlImportService owlImportService;

  @RequestMapping(value = "owlprocessmodel", method = RequestMethod.POST)
  public @ResponseBody Callable<OWLProcessModelDTO> getOwlProcessModel(
          @RequestBody final Map<String, String> requestBody, final HttpServletRequest request) {
    return () -> {
      return owlImportService.getOwlProcessModelDTO(requestBody.get("owlContent"), requestBody.get("version")).get();
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
