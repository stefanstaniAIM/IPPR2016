package at.fhjoanneum.ippr.communicator.services;

import at.fhjoanneum.ippr.commons.dto.communicator.ExternalCommunicatorMessage;
import at.fhjoanneum.ippr.commons.dto.communicator.ReceiveSubmissionDTO;

public interface ExternalCommunicatorService {

  void handleExternalOutputMessage(ExternalCommunicatorMessage message);

  void handleExternalInputMessage(String body, String endpoint);

  void handleReceiveSubmission(ReceiveSubmissionDTO receiveSubmission);
}
