package at.fhjoanneum.ippr.pmstorage.services.impl;

import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import at.fhjoanneum.ippr.commons.dto.owlimport.OWLProcessModelDTO;
import at.fhjoanneum.ippr.pmstorage.examples.VacationRequestFromOWL;
import at.fhjoanneum.ippr.pmstorage.services.OwlImportService;

@Service
public class OwlImportServiceImpl implements OwlImportService {

  @Autowired
  private VacationRequestFromOWL vacationRequestFromOWL;

  @Async
  @Override
  public Future<OWLProcessModelDTO> getOwlProcessModelDTO() {
    final OWLProcessModelDTO owlProcessModel = vacationRequestFromOWL.getProcessModelDTO();
    return new AsyncResult<OWLProcessModelDTO>(owlProcessModel);
  }
}
