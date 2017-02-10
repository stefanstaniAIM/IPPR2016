package at.fhjoanneum.ippr.gateway.api.controller;

import java.io.ByteArrayInputStream;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

  @RequestMapping(value = "api/owlprocessmodel", method = RequestMethod.GET)
  public @ResponseBody Callable<ResponseEntity<OWLProcessModelDTO>> getOWLProcessModel(
      final HttpServletRequest request) {
    return () -> {
      return owlImportGatewayCaller.getOWLProcessModel().get();
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

  @RequestMapping(value = "api/owlupload", method = RequestMethod.POST)
  public @ResponseBody Callable<ResponseEntity<?>> uploadOwlFile(
      @RequestParam("uploadfile") final MultipartFile file, final HttpServletRequest request) {
    return () -> {
      try {
        final ByteArrayInputStream stream = new ByteArrayInputStream(file.getBytes());
        final String owlContent = IOUtils.toString(stream, "UTF-8");
        LOG.debug("owl input is: {}", owlContent);
        final HttpHeaderUser user = new HttpHeaderUser(request);
        return new ResponseEntity<String>(owlContent, HttpStatus.OK);
      } catch (final Exception e) {
        LOG.error(e.getMessage());
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
      }
    };
  }

}
