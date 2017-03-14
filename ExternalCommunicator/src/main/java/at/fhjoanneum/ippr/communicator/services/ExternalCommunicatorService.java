package at.fhjoanneum.ippr.communicator.services;

import at.fhjoanneum.ippr.commons.dto.communicator.ExternalOutputMessage;

public interface ExternalCommunicatorService {

  void handleExternalOutputMessage(ExternalOutputMessage message);
}
