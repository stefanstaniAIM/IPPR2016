package at.fhjoanneum.ippr.pmstorage.services.impl;


import java.util.List;
import java.util.concurrent.Future;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import at.fhjoanneum.ippr.commons.dto.pmstorage.ProcessModelDTO;
import at.fhjoanneum.ippr.commons.dto.pmstorage.SubjectModelDTO;
import at.fhjoanneum.ippr.persistence.entities.model.process.ProcessModelImpl;
import at.fhjoanneum.ippr.pmstorage.repositories.ProcessModelRepository;
import at.fhjoanneum.ippr.pmstorage.services.ProcessModelService;


@Service
public class ProcessModelServiceImpl implements ProcessModelService {

  @Autowired
  private ProcessModelRepository processModelRepository;

  @Override
  @Async
  @Transactional
  public Future<List<ProcessModelDTO>> findActiveProcessModels(final Pageable pageable) {
    final Page<ProcessModelImpl> results = processModelRepository.findActiveProcesses(pageable);

    final List<ProcessModelDTO> processModels = createProcessModelDTO(results);

    return new AsyncResult<List<ProcessModelDTO>>(processModels);
  }

  @Override
  @Async
  @Transactional
  public Future<List<ProcessModelDTO>> findActiveProcessModelsToStart(final List<String> groups,
      final Pageable pageable) {
    final Page<ProcessModelImpl> results =
        processModelRepository.findActiveProcessesToStart(pageable, groups);

    final List<ProcessModelDTO> processModels = createProcessModelDTO(results);
    return new AsyncResult<List<ProcessModelDTO>>(processModels);
  }

  private static List<ProcessModelDTO> createProcessModelDTO(final Page<ProcessModelImpl> results) {
    final List<ProcessModelDTO> processModels = Lists.newArrayList();
    final List<SubjectModelDTO> subjectModels = Lists.newArrayList();

    results.forEach(process -> {
      process.getSubjectModels().forEach(subject -> {
        subjectModels
            .add(new SubjectModelDTO(subject.getSmId(), subject.getName(), subject.getGroup()));
      });;

      final ProcessModelDTO dto = new ProcessModelDTO(process.getPmId(), process.getName(),
          process.getDescription(), process.createdAt(), subjectModels);
      processModels.add(dto);
    });

    return processModels;
  }
}
