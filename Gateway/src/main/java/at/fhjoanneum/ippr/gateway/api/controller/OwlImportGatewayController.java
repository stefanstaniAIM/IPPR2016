package at.fhjoanneum.ippr.gateway.api.controller;

import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import at.fhjoanneum.ippr.commons.dto.owlimport.OWLProcessModelDTO;
import at.fhjoanneum.ippr.gateway.api.services.impl.OwlImportGatewayCallerImpl;

@RestController
@RequestMapping(produces = "application/json; charset=UTF-8")
public class OwlImportGatewayController {

  @Autowired
  private OwlImportGatewayCallerImpl owlImportGatewayCaller;

  @RequestMapping(value = "api/owlprocessmodel", method = RequestMethod.GET)
  public @ResponseBody Callable<ResponseEntity<OWLProcessModelDTO>> getOWLProcessModel(
      final HttpServletRequest request) {
    return () -> {
      return owlImportGatewayCaller.getOWLProcessModel().get();
    };
  }
}
