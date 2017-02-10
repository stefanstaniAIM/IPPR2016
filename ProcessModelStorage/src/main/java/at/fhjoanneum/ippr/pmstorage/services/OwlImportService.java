package at.fhjoanneum.ippr.pmstorage.services;

import java.util.concurrent.Future;

import at.fhjoanneum.ippr.commons.dto.owlimport.jsonimport.ImportProcessModelDTO;
import at.fhjoanneum.ippr.commons.dto.owlimport.reader.OWLProcessModelDTO;

public interface OwlImportService {

  Future<OWLProcessModelDTO> getOwlProcessModelDTO(String owlContent);

  Future<Boolean> importProcessModel(ImportProcessModelDTO processModelDTO);
}
