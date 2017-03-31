package at.fhjoanneum.ippr.communicator.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import akka.actor.ActorRef;
import at.fhjoanneum.ippr.commons.dto.communicator.ExternalCommunicatorMessage;
import at.fhjoanneum.ippr.commons.dto.communicator.ReceiveSubmissionDTO;
import at.fhjoanneum.ippr.communicator.akka.messages.compose.commands.ComposeMessageCreateCommand;
import at.fhjoanneum.ippr.communicator.akka.messages.parse.commands.ParseMessageCreateCommand;
import at.fhjoanneum.ippr.communicator.global.GlobalKey;
import at.fhjoanneum.ippr.communicator.persistence.entities.config.ConfigurationAssignement;
import at.fhjoanneum.ippr.communicator.persistence.entities.submission.ReceiveSubmission;
import at.fhjoanneum.ippr.communicator.persistence.entities.submission.ReceiveSubmissionBuilder;
import at.fhjoanneum.ippr.communicator.persistence.objects.DataType;
import at.fhjoanneum.ippr.communicator.persistence.objects.basic.inbound.BasicInboundConfiguration;
import at.fhjoanneum.ippr.communicator.persistence.objects.internal.InternalData;
import at.fhjoanneum.ippr.communicator.persistence.objects.internal.InternalField;
import at.fhjoanneum.ippr.communicator.persistence.objects.internal.InternalObject;
import at.fhjoanneum.ippr.communicator.repositories.BasicInboundConfigurationRepository;
import at.fhjoanneum.ippr.communicator.repositories.ConfigurationAssignementRepository;
import at.fhjoanneum.ippr.communicator.repositories.ReceiveSubmissionRepository;

@Service
public class ExternalCommunicatorServiceImpl implements ExternalCommunicatorService {

  private final static Logger LOG = LoggerFactory.getLogger(ExternalCommunicatorServiceImpl.class);

  @Autowired
  private ActorRef composeSupervisorActor;

  @Autowired
  private ActorRef parseSupervisorActor;

  @Autowired
  private ConfigurationAssignementRepository configurationAssignmentRepository;

  @Autowired
  private BasicInboundConfigurationRepository basicInboundCongurationRepository;

  @Autowired
  private ReceiveSubmissionRepository receiveSubmissionRepository;

  @Async
  @Override
  public void handleExternalOutputMessage(final ExternalCommunicatorMessage message) {
    LOG.debug("Received request for external out message [{}]", message);

    final Long messageFlowId = getMessageFlowId(message.getTransferId());
    final Long configId = getActiveConfig(messageFlowId);

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

  private Long getActiveConfig(final Long messageFlowId) {
    final ConfigurationAssignement assignement =
        configurationAssignmentRepository.findByMessageFlowId(messageFlowId);
    if (assignement.getOutboundConfiguration() != null) {
      return assignement.getOutboundConfiguration().getId();
    } else {
      return assignement.getInboundConfiguration().getId();
    }
  }

  @Override
  public void handleExternalInputMessage(final String body, final String endpoint) {
    final Optional<BasicInboundConfiguration> configOpt = Optional
        .ofNullable(basicInboundCongurationRepository.findByEndpoint(GlobalKey.ENDPOINT, endpoint));

    final BasicInboundConfiguration config =
        configOpt.orElseThrow(() -> new IllegalArgumentException("No config found!"));
    parseSupervisorActor.tell(new ParseMessageCreateCommand(body, config.getId()),
        ActorRef.noSender());
  }

  @Override
  public void handleReceiveSubmission(final ReceiveSubmissionDTO receiveSubmission) {
    final Long configId = getActiveConfig(getMessageFlowId(receiveSubmission.getTransferId()));

    final Optional<BasicInboundConfiguration> configOpt =
        Optional.ofNullable(basicInboundCongurationRepository.findOne(configId));

    final BasicInboundConfiguration config =
        configOpt.orElseThrow(() -> new IllegalArgumentException("No config found!"));

    final ReceiveSubmission entry = new ReceiveSubmissionBuilder()
        .transferId(receiveSubmission.getTransferId()).basicInboundConfiguration(config).build();
    receiveSubmissionRepository.save(entry);
    LOG.debug("New receive submission [{}]", entry);
  }
}
