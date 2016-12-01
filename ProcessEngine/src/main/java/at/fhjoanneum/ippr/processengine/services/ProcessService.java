package at.fhjoanneum.ippr.processengine.services;

import java.util.concurrent.Future;

import at.fhjoanneum.ippr.commons.dto.processengine.ProcessStartDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.ProcessStartedDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.ProcessStateDTO;

public interface ProcessService {

  Future<ProcessStartedDTO> startProcess(final ProcessStartDTO processStartDTO);

  Future<Long> getAmountOfActiveProcesses();

  Future<Long> getAmountOfActiveProcessesPerUser(Long userId);

  Future<ProcessStateDTO> getStateOfProcessInstance(Long piId);
}
