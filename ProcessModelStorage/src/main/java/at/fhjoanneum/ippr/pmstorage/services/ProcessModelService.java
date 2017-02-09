package at.fhjoanneum.ippr.pmstorage.services;

import java.util.List;
import java.util.concurrent.Future;

import org.springframework.data.domain.Pageable;

import at.fhjoanneum.ippr.commons.dto.pmstorage.FieldPermissionDTO;
import at.fhjoanneum.ippr.commons.dto.pmstorage.FieldTypeDTO;
import at.fhjoanneum.ippr.commons.dto.pmstorage.ProcessModelDTO;

public interface ProcessModelService {

  Future<List<ProcessModelDTO>> findActiveProcessModels(final Pageable pageable);

  Future<List<ProcessModelDTO>> findActiveProcessModelsToStart(List<String> groups,
      final Pageable pageable);

  Future<List<ProcessModelDTO>> findAllProcessModels();

  Future<List<FieldTypeDTO>> getFieldTypes();

  Future<List<FieldPermissionDTO>> getPermissions();

  void disableProcessModel(Long pmId);
}
