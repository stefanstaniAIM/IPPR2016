package at.fhjoanneum.ippr.pmstorage.controller;

import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import at.fhjoanneum.ippr.commons.dto.owlimport.OWLProcessModelDTO;
import at.fhjoanneum.ippr.pmstorage.services.OwlImportService;

@RestController
@RequestMapping(produces = "application/json; charset=UTF-8")
public class OwlImportController {

  @Autowired
  private OwlImportService owlImportService;

  @RequestMapping(value = "owlprocessmodel", method = RequestMethod.GET)
  public @ResponseBody Callable<OWLProcessModelDTO> getOwlProcessModel(
      final HttpServletRequest request) {
    return () -> {
      return owlImportService.getOwlProcessModelDTO().get();
    };
  }
}
