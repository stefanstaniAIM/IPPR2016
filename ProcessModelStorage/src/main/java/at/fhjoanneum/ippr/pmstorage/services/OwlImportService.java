package at.fhjoanneum.ippr.pmstorage.services;

import at.fhjoanneum.ippr.commons.dto.owlimport.jsonimport.ImportProcessModelDTO;
import at.fhjoanneum.ippr.commons.dto.owlimport.reader.OWLProcessModelDTO;

import java.util.concurrent.Future;

public interface OwlImportService {

  Future<OWLProcessModelDTO> getOwlProcessModelDTO(String owlContent, String version);

  Future<Boolean> importProcessModel(ImportProcessModelDTO processModelDTO);
}
