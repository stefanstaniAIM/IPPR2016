package at.fhjoanneum.ippr.gateway.api.controller;

import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import at.fhjoanneum.ippr.commons.dto.owlimport.jsonimport.ImportProcessModelDTO;
import at.fhjoanneum.ippr.commons.dto.owlimport.reader.OWLProcessModelDTO;
import at.fhjoanneum.ippr.gateway.api.controller.user.HttpHeaderUser;
import at.fhjoanneum.ippr.gateway.api.services.impl.OwlImportGatewayCallerImpl;

@RestController
@RequestMapping(produces = "application/json; charset=UTF-8")
public class OwlImportGatewayController {

  private final static Logger LOG = LoggerFactory.getLogger(OwlImportGatewayController.class);

  @Autowired
  private OwlImportGatewayCallerImpl owlImportGatewayCaller;

  @RequestMapping(value = "api/owlprocessmodel", method = RequestMethod.POST)
  public @ResponseBody Callable<ResponseEntity<OWLProcessModelDTO>> getOWLProcessModel(
      final @RequestBody String owlContent, final HttpServletRequest request) {
    return () -> {
      final HttpHeaderUser user = new HttpHeaderUser(request);
      return owlImportGatewayCaller.getOWLProcessModel(owlContent, user).get();
    };
  }

  @RequestMapping(value = "api/import", method = RequestMethod.POST)
  public @ResponseBody Callable<ResponseEntity<Boolean>> importProcess(
      final @RequestBody ImportProcessModelDTO processModelDTO, final HttpServletRequest request) {
    return () -> {
      final HttpHeaderUser user = new HttpHeaderUser(request);
      return owlImportGatewayCaller.importProcessModel(processModelDTO, user).get();
    };
  }
}
