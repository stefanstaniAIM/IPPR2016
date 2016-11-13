package at.fhjoanneum.ippr.pmstorage.services.impl;


import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import at.fhjoanneum.ippr.persistence.entities.model.process.ProcessModelImpl;
import at.fhjoanneum.ippr.pmstorage.dto.ProcessModelDTO;
import at.fhjoanneum.ippr.pmstorage.dto.SubjectModelDTO;
import at.fhjoanneum.ippr.pmstorage.repositories.ProcessModelRepository;


@Service
public class ProcessModelServiceImpl {

  @Autowired
  private ProcessModelRepository processModelRepository;

  @Async
  @Transactional(readOnly = true)
  public Future<List<ProcessModelDTO>> findActiveProcessModels(final Pageable pageable) {
    final Page<ProcessModelImpl> results = processModelRepository.findActiveProcesses(pageable);

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

    return new AsyncResult<List<ProcessModelDTO>>(processModels);
  }
}
