package at.fhjoanneum.ippr.processengine.services;

import java.util.List;
import java.util.concurrent.Future;

import at.fhjoanneum.ippr.commons.dto.processengine.ProcessInfoDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.ProcessStartDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.ProcessStartedDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.ProcessStateDTO;

public interface ProcessService {

  Future<ProcessStartedDTO> startProcess(final ProcessStartDTO processStartDTO);

  Future<Long> getAmountOfActiveProcesses();

  Future<Long> getAmountOfActiveProcessesPerUser(Long userId);

  Future<ProcessStateDTO> getStateOfProcessInstance(Long piId);

  Future<List<ProcessInfoDTO>> getProcessesInfoOfState(String state, int page, int size);

  Future<List<ProcessInfoDTO>> getProcessesInfoOfUserAndState(Long user, String state, int page,
      int size);

  Future<ProcessInfoDTO> stopProcess(Long piId);
}
