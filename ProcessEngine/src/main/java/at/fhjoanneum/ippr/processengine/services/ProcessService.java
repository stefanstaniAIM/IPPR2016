package at.fhjoanneum.ippr.processengine.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Future;

import at.fhjoanneum.ippr.commons.dto.processengine.ProcessInfoDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.ProcessStartDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.ProcessStartedDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.ProcessStateDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.TaskDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.stateobject.StateObjectChangeDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.stateobject.StateObjectDTO;

public interface ProcessService {

  Future<ProcessStartedDTO> startProcess(final ProcessStartDTO processStartDTO);

  Future<Long> getAmountOfProcessesInState(String state);

  Future<Long> getAmountOfProcessesInStatePerUser(String state, Long userId);

  Future<Long> getAmountOfStartedProcessesBetween(LocalDateTime start, LocalDateTime end);

  Future<Long> getAmountOfStartedProcessesBetweenForUser(LocalDateTime start, LocalDateTime end,
      Long userId);

  Future<Long> getAmountOfFinishedProcessesBetween(LocalDateTime start, LocalDateTime end);

  Future<Long> getAmountOfFinishedProcessesBetweenForUser(LocalDateTime start, LocalDateTime end,
      Long userId);

  Future<ProcessStateDTO> getStateOfProcessInstance(Long piId);

  Future<List<ProcessInfoDTO>> getProcessesInfoOfState(String state, int page, int size);

  Future<List<ProcessInfoDTO>> getProcessesInfoOfUserAndState(Long user, String state, int page,
      int size);

  Future<ProcessInfoDTO> stopProcess(Long piId);

  Future<List<TaskDTO>> getTasksOfUser(Long userId);

  Future<StateObjectDTO> getStateObjectOfUserInProcess(Long piId, Long userId);

  Future<Boolean> changeStateOfUserInProcess(final Long piId, final Long userId,
      final StateObjectChangeDTO stateObjectChangeDTO);
}
