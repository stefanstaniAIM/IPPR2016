package at.fhjoanneum.ippr.processengine.feign;

import at.fhjoanneum.ippr.commons.dto.communicator.ExternalCommunicatorMessage;

public interface ProcessEngineFeignService {

  void markAsSent(String transferId);

  void storeExternalCommunicatorMessage(ExternalCommunicatorMessage message);
}
