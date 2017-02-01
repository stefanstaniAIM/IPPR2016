package at.fhjoanneum.ippr.pmstorage.services;

import java.util.concurrent.Future;

import at.fhjoanneum.ippr.commons.dto.owlimport.OWLProcessModelDTO;

public interface OwlImportService {

  Future<OWLProcessModelDTO> getOwlProcessModelDTO();
}
