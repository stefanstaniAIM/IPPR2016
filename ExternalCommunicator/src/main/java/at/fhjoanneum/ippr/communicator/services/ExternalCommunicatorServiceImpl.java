package at.fhjoanneum.ippr.communicator.services;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import akka.actor.ActorRef;
import at.fhjoanneum.ippr.commons.dto.communicator.ExternalOutputMessage;
import at.fhjoanneum.ippr.communicator.akka.messages.compose.commands.ComposeMessageCreateCommand;
import at.fhjoanneum.ippr.communicator.persistence.objects.DataType;
import at.fhjoanneum.ippr.communicator.persistence.objects.internal.InternalData;
import at.fhjoanneum.ippr.communicator.persistence.objects.internal.InternalField;
import at.fhjoanneum.ippr.communicator.persistence.objects.internal.InternalObject;
import at.fhjoanneum.ippr.communicator.repositories.OutboundConfigurationMapRepository;

@Service
public class ExternalCommunicatorServiceImpl implements ExternalCommunicatorService {

  private final static Logger LOG = LoggerFactory.getLogger(ExternalCommunicatorServiceImpl.class);

  @Autowired
  private ActorRef composeSupervisorActor;

  @Autowired
  private OutboundConfigurationMapRepository outboundConfigurationMapRepository;

  @Async
  @Override
  public void handleExternalOutputMessage(final ExternalOutputMessage message) {
    LOG.debug("Received request for external out message [{}]", message);

    final Long messageFlowId = getMessageFlowId(message.getTransferId());
    final Long configId = getActiveOutboundConfig(messageFlowId);

    final Map<String, InternalObject> businessObjects = new HashMap<>();
    message.getBusinessObjects().stream().forEachOrdered(bo -> {
      final Map<String, InternalField> fields = new HashMap<>();
      bo.getFields().stream().forEachOrdered(field -> {
        fields.put(field.getName(), new InternalField(field.getName(),
            DataType.valueOf(field.getType()), field.getValue()));
      });
      businessObjects.put(bo.getName(), new InternalObject(bo.getName(), fields));
    });

    composeSupervisorActor.tell(new ComposeMessageCreateCommand(message.getTransferId(),
        new InternalData(businessObjects), configId), ActorRef.noSender());
  }

  private Long getMessageFlowId(final String transferId) {
    return Long.valueOf(transferId.split("-")[2]);
  }

  private Long getActiveOutboundConfig(final Long messageFlowId) {
    return outboundConfigurationMapRepository.findByMessageFlowId(messageFlowId).getId();
  }
}
