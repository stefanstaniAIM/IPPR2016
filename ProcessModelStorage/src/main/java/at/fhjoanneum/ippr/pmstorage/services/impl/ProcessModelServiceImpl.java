package at.fhjoanneum.ippr.pmstorage.services.impl;


import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import at.fhjoanneum.ippr.commons.dto.owlimport.OWLProcessModelDTO;
import at.fhjoanneum.ippr.pmstorage.examples.VacationRequestFromOWL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import at.fhjoanneum.ippr.commons.dto.pmstorage.FieldTypeDTO;
import at.fhjoanneum.ippr.commons.dto.pmstorage.ProcessModelDTO;
import at.fhjoanneum.ippr.commons.dto.pmstorage.SubjectModelDTO;
import at.fhjoanneum.ippr.persistence.entities.model.process.ProcessModelImpl;
import at.fhjoanneum.ippr.persistence.objects.model.enums.FieldType;
import at.fhjoanneum.ippr.pmstorage.repositories.ProcessModelRepository;
import at.fhjoanneum.ippr.pmstorage.services.ProcessModelService;


@Service
public class ProcessModelServiceImpl implements ProcessModelService {

  private static final Logger LOG = LoggerFactory.getLogger(ProcessModelServiceImpl.class);

  @Autowired
  private ProcessModelRepository processModelRepository;

  @Autowired
  private VacationRequestFromOWL vacationRequestFromOWL;

  @Override
  @Async
  @Transactional
  public Future<List<ProcessModelDTO>> findActiveProcessModels(final Pageable pageable) {
    final List<ProcessModelImpl> results = processModelRepository.findActiveProcesses();

    final List<ProcessModelDTO> processModels = createProcessModelDTO(results);

    return new AsyncResult<List<ProcessModelDTO>>(processModels);
  }

  @Override
  @Async
  @Transactional
  public Future<List<ProcessModelDTO>> findActiveProcessModelsToStart(final List<String> rules,
      final Pageable pageable) {
    final List<ProcessModelImpl> results = processModelRepository.findActiveProcessesToStart(rules);

    final List<ProcessModelDTO> processModels = createProcessModelDTO(results);
    return new AsyncResult<List<ProcessModelDTO>>(processModels);
  }

  private static List<ProcessModelDTO> createProcessModelDTO(final List<ProcessModelImpl> results) {
    final List<ProcessModelDTO> processModels = Lists.newArrayList();
    final List<SubjectModelDTO> subjectModels = Lists.newArrayList();

    results.forEach(process -> {
      process.getSubjectModels().forEach(subject -> {
        subjectModels.add(new SubjectModelDTO(subject.getSmId(), subject.getName(),
            subject.getGroup(), subject.getAssignedRules()));
      });

      final ProcessModelDTO dto = new ProcessModelDTO(process.getPmId(), process.getName(),
          process.getDescription(), process.createdAt(), subjectModels);
      processModels.add(dto);
    });

    return processModels;
  }

  @Async
  @Override
  public Future<List<FieldTypeDTO>> getFieldTypes() {
    final List<FieldTypeDTO> fieldTypes = Arrays.stream(FieldType.values())
        .map(fieldtype -> new FieldTypeDTO(fieldtype.name())).collect(Collectors.toList());
    return new AsyncResult<List<FieldTypeDTO>>(fieldTypes);
  }

  @Override
  @Async
  @Transactional
  public Future<OWLProcessModelDTO> getOWLProcessModel() {
    final OWLProcessModelDTO owlProcessModel = vacationRequestFromOWL.getProcessModelDTO();
    return new AsyncResult<OWLProcessModelDTO>(owlProcessModel);
  }
}
