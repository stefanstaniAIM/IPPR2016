package at.fhjoanneum.ippr.communicator.services;

import at.fhjoanneum.ippr.commons.dto.communicator.ExternalOutputMessage;
import at.fhjoanneum.ippr.commons.dto.communicator.ReceiveSubmissionDTO;

public interface ExternalCommunicatorService {

  void handleExternalOutputMessage(ExternalOutputMessage message);

  void handleExternalInputMessage(String body, String endpoint);

  void handleReceiveSubmission(ReceiveSubmissionDTO receiveSubmission);
}
