package at.fhjoanneum.ippr.processengine.akka.tasks.user;

import akka.actor.ActorRef;
import akka.actor.Status;
import akka.pattern.Patterns;
import akka.pattern.PatternsCS;
import at.fhjoanneum.ippr.commons.dto.communicator.BusinessObject;
import at.fhjoanneum.ippr.commons.dto.communicator.ExternalCommunicatorMessage;
import at.fhjoanneum.ippr.commons.dto.communicator.ReceiveSubmissionDTO;
import at.fhjoanneum.ippr.commons.dto.eventlogger.EventLoggerDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.SendProcessMessage;
import at.fhjoanneum.ippr.commons.dto.processengine.stateobject.BusinessObjectInstanceDTO;
import at.fhjoanneum.ippr.persistence.entities.engine.businessobject.BusinessObjectInstanceBuilder;
import at.fhjoanneum.ippr.persistence.entities.engine.businessobject.BusinessObjectInstanceImpl;
import at.fhjoanneum.ippr.persistence.entities.engine.businessobject.field.BusinessObjectFieldInstanceBuilder;
import at.fhjoanneum.ippr.persistence.entities.engine.businessobject.field.BusinessObjectFieldInstanceImpl;
import at.fhjoanneum.ippr.persistence.entities.engine.enums.SubjectSubState;
import at.fhjoanneum.ippr.persistence.entities.engine.state.SubjectStateImpl;
import at.fhjoanneum.ippr.persistence.objects.engine.businessobject.BusinessObjectFieldInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.businessobject.BusinessObjectInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.process.ProcessInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.state.SubjectState;
import at.fhjoanneum.ippr.persistence.objects.engine.subject.Subject;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.BusinessObjectModel;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.permission.BusinessObjectFieldPermission;
import at.fhjoanneum.ippr.persistence.objects.model.enums.FieldPermission;
import at.fhjoanneum.ippr.persistence.objects.model.enums.FieldType;
import at.fhjoanneum.ippr.persistence.objects.model.enums.StateFunctionType;
import at.fhjoanneum.ippr.persistence.objects.model.enums.SubjectModelType;
import at.fhjoanneum.ippr.persistence.objects.model.messageflow.MessageFlow;
import at.fhjoanneum.ippr.persistence.objects.model.state.State;
import at.fhjoanneum.ippr.processengine.akka.config.Global;
import at.fhjoanneum.ippr.processengine.akka.config.SpringExtension;
import at.fhjoanneum.ippr.processengine.akka.messages.EmptyMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.refinement.ExecuteRefinementMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.timeout.TimeoutScheduleStartMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.workflow.AssignUsersMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.workflow.MessagesSendMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.workflow.StateObjectChangeMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.workflow.StateObjectChangeMessage.Request;
import at.fhjoanneum.ippr.processengine.akka.tasks.AbstractTask;
import at.fhjoanneum.ippr.processengine.feign.ExternalCommunicatorClient;
import at.fhjoanneum.ippr.processengine.parser.DbValueParser;
import at.fhjoanneum.ippr.processengine.repositories.*;
import at.fhjoanneum.ippr.processengine.services.EventLoggerSender;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import scala.concurrent.Await;
import scala.concurrent.Future;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component("User.StateObjectChangeTask")
@Scope("prototype")
public class StateObjectChangeTask extends AbstractTask<StateObjectChangeMessage.Request> {

  private final static Logger LOG = LoggerFactory.getLogger(StateObjectChangeTask.class);

  @Autowired
  private SpringExtension springExtension;
  @Autowired
  private ProcessInstanceRepository processInstanceRepository;
  @Autowired
  private SubjectStateRepository subjectStateRepository;
  @Autowired
  private BusinessObjectInstanceRepository businessObjectInstanceRepository;
  @Autowired
  private BusinessObjectFieldInstanceRepository businessObjectFieldInstanceRepository;
  @Autowired
  private BusinessObjectFieldPermissionRepository businessObjectFieldPermissionRepository;
  @Autowired
  private StateRepository stateRepository;
  @Autowired
  private SubjectRepository subjectRepository;
  @Autowired
  private MessageFlowRepository messageFlowRepository;
  @Autowired
  private EventLoggerSender eventLoggerSender;

  @Autowired
  private ExternalCommunicatorClient externalCommunicatorClient;

  @Autowired
  private DbValueParser valueParser;

  @PersistenceContext
  private EntityManager entityManager;

  private ActorRef sender;

  @Override
  public boolean canHandle(final Object obj) {
    return obj instanceof StateObjectChangeMessage.Request;
  }

  @Override
  public void execute(final StateObjectChangeMessage.Request request) throws Exception {
    handleStateObjectChangeMessage(request);
  }

  private void handleStateObjectChangeMessage(final StateObjectChangeMessage.Request request)
      throws Exception {
    final SubjectState subjectState = Optional
        .ofNullable(
            subjectStateRepository.getSubjectStateOfUser(request.getPiId(), request.getUserId()))
        .get();

    sender = getSender();

    final ActorRef bussinessObjectCheckActor = getContext().actorOf(
        springExtension.props("BusinessObjectCheckActor", subjectState.getCurrentState().getSId()),
        UUID.randomUUID().toString());

    // must block thread since transaction is lost when using completable future
    final Future<Object> future = Patterns.ask(bussinessObjectCheckActor, request, Global.TIMEOUT);
    final boolean correct =
        ((Boolean) Await.result(future, Global.TIMEOUT.duration())).booleanValue();

    if (!correct) {
      sender.tell(new Status.Failure(
          new IllegalArgumentException("Check of business objects returned false")), getSelf());
    } else {
      initBusinessObjectInstances(subjectState, request);
      setValuesOfBusinessObjectFieldInstances(subjectState.getCurrentState(), request);
      sendMessages(subjectState, request);

      TransactionSynchronizationManager
          .registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
              sender.tell(new EmptyMessage(), getSelf());
              handleAdditionalActions(subjectState);
            }
          });
    }
  }

  private void initBusinessObjectInstances(final SubjectState state,
      final StateObjectChangeMessage.Request request) {
    final ProcessInstance processInstance = processInstanceRepository.findOne(request.getPiId());

    final List<BusinessObjectModel> toCreate = state.getCurrentState().getBusinessObjectModels()
        .stream().map(BusinessObjectModel::flattened).flatMap(List::stream)
        .filter(model -> !processInstance.isBusinessObjectInstanceOfModelCreated(model))
        .collect(Collectors.toList());

    LOG.debug("Must create instances for business object models: {}", toCreate);
    toCreate.forEach(model -> createBusinessObjectInstanceOfModel(processInstance, model));
  }

  private void createBusinessObjectInstanceOfModel(final ProcessInstance processInstance,
      final BusinessObjectModel businessObjectModel) {
    final BusinessObjectInstance businessObjectInstance = new BusinessObjectInstanceBuilder()
        .processInstance(processInstance).businessObjectModel(businessObjectModel).build();

    final List<BusinessObjectFieldInstanceImpl> fields =
        businessObjectModel.getBusinessObjectFieldModels().stream()
            .map(fieldModel -> new BusinessObjectFieldInstanceBuilder()
                .businessObjectInstance(businessObjectInstance).businessObjectFieldModel(fieldModel)
                .build())
            .map(field -> (BusinessObjectFieldInstanceImpl) field).collect(Collectors.toList());

    businessObjectInstanceRepository.save((BusinessObjectInstanceImpl) businessObjectInstance);
    businessObjectFieldInstanceRepository.save(fields);
    LOG.info("Created new business object instance: {}", businessObjectInstance);
  }

  private void setValuesOfBusinessObjectFieldInstances(final State currentState,
      final StateObjectChangeMessage.Request request) {
    final ActorRef sender = getSender();

    if (request.getStateObjectChangeDTO().getBusinessObjects() == null) {
      return;
    }

    final Stream<BusinessObjectInstanceDTO> businessObjects =
        request.getStateObjectChangeDTO().getBusinessObjects().stream()
            .map(BusinessObjectInstanceDTO::flattened).flatMap(List::stream);

    businessObjects.map(BusinessObjectInstanceDTO::getFields).flatMap(List::stream)
        .forEach(field -> {
          final Optional<BusinessObjectFieldPermission> permissionOpt = Optional.ofNullable(
              businessObjectFieldPermissionRepository.getBusinessObjectFieldPermissionInState(
                  field.getBofmId(), currentState.getSId()));

          if (permissionOpt.isPresent()) {
            final BusinessObjectFieldPermission permission = permissionOpt.get();

            if (permission.getPermission().equals(FieldPermission.READ_WRITE)
                && StringUtils.isNotBlank(field.getValue())) {
              final Optional<BusinessObjectFieldInstance> fieldInstanceOpt =
                  Optional.ofNullable(businessObjectFieldInstanceRepository
                      .getBusinessObjectFieldInstanceForModelInProcess(request.getPiId(),
                          field.getBofmId()));
              if (!fieldInstanceOpt.isPresent()) {
                sender.tell(
                    new akka.actor.Status.Failure(new IllegalStateException(
                        "Could not find field instance for BOFM_ID [" + field.getBofmId() + "]")),
                    getSelf());
              } else {
                // parse the value
                final BusinessObjectFieldInstance fieldInstance = fieldInstanceOpt.get();
                final FieldType fieldType =
                    fieldInstance.getBusinessObjectFieldModel().getFieldType();
                final String value = valueParser.parse(field.getValue(), fieldType);
                LOG.debug("Parsed value is: {}", value);
                fieldInstance.setValue(value);
                businessObjectFieldInstanceRepository
                    .save((BusinessObjectFieldInstanceImpl) fieldInstance);
                LOG.info("Updated the value of field instance: {} to {}", fieldInstance, value);
              }
            }
          }
        });
  }

  private void sendMessages(final SubjectState subjectState,
      final StateObjectChangeMessage.Request request) {
    if (StateFunctionType.SEND.equals(subjectState.getCurrentState().getFunctionType())
        && SubjectSubState.TO_SEND.equals(subjectState.getSubState())) {
      assignUsers(request, subjectState);
    } else {
      changeToNextState(subjectState, request);
    }
  }

  private void assignUsers(final StateObjectChangeMessage.Request request,
      final SubjectState subjectState) {
    try {
      if (request.getStateObjectChangeDTO().getUserAssignments() == null
          || request.getStateObjectChangeDTO().getUserAssignments().isEmpty()) {
        LOG.debug("All user assignements are done at the moment for P_ID [{}]", request.getPiId());
      } else {
        PatternsCS
            .ask(getContext().parent(),
                new AssignUsersMessage.Request(request.getPiId(),
                    request.getStateObjectChangeDTO().getUserAssignments()),
                Global.TIMEOUT)
            .toCompletableFuture().get();
      }

      triggerSendInternal(subjectState, request);
      triggerSendExternal(subjectState, request);
      triggerSendProcess(subjectState, request);

    } catch (final Exception e) {
      LOG.error(e.getMessage());
      sender.tell(new Status.Failure(new IllegalStateException("Error: " + e.getMessage())),
          getSelf());
    }
  }

  private void triggerSendProcess(final SubjectState subjectState, final Request request) {
    subjectState.getCurrentState().getMessageFlow().stream()
        .filter(mf -> SubjectModelType.PROCESS.equals(mf.getReceiver().getSubjectModelType()))
        .forEachOrdered(mf -> {
          final BusinessObjectInstance boInstance =
              businessObjectInstanceRepository.getBusinessObjectInstanceOfModelInProcess(
                  request.getPiId(), mf.getBusinessObjectModels().get(0).getBomId());

          try {
            PatternsCS
                .ask(getContext().parent(),
                    new SendProcessMessage.Request(request.getPiId(),
                        subjectState.getSubject().getSId(),
                        request.getStateObjectChangeDTO().getUserAssignments() == null
                            || request.getStateObjectChangeDTO().getUserAssignments().isEmpty()
                                ? null
                                : request.getStateObjectChangeDTO().getUserAssignments().get(0)
                                    .getUserId(),
                        mf.getMfId(), boInstance != null ? boInstance.getBoiId() : null),
                    Global.TIMEOUT)
                .toCompletableFuture().get();

            subjectState.setToSent();
            changeToNextState(subjectState, request);
          } catch (final Exception e) {
            LOG.error(e.getMessage());
            sender.tell(new Status.Failure(new IllegalStateException("Error: " + e.getMessage())),
                getSelf());
          }
        });
  }

  private void triggerSendInternal(final SubjectState subjectState,
      final StateObjectChangeMessage.Request request) {
    final ActorRef userActor = getContext().parent();

    final List<Pair<Long, Long>> userMessageFlowIds =
        subjectState.getCurrentState().getMessageFlow().stream()
            .filter(mf -> SubjectModelType.INTERNAL.equals(mf.getReceiver().getSubjectModelType()))
            .map(messageFlow -> getUserMessageFlowIds(request.getPiId(), messageFlow))
            .collect(Collectors.toList());

    // Log Send Event
    final long caseId = subjectState.getProcessInstance().getPiId();
    final long processModelId = subjectState.getProcessInstance().getProcessModel().getPmId();
    final String activity = subjectState.getCurrentState().getName();
    final String state = StateFunctionType.SEND.name();
    final String resource = subjectState.getSubject().getSubjectModel().getName();
    final String timestamp = DateTime.now().toString("dd.MM.yyyy HH:mm");
    final MessageFlow messageFlow = messageFlowRepository.findOne(subjectState.getCurrentState().getMessageFlow().get(0).getMfId());
    final String messageType = messageFlow
            .getBusinessObjectModels().get(0).getName();
    final String recipient = subjectState.getCurrentState().getMessageFlow().get(0).getReceiver().getName();
    final String msgSender = resource;


    final EventLoggerDTO event = new EventLoggerDTO(caseId, processModelId, timestamp, activity,
        resource, state, messageType, recipient, msgSender);
    eventLoggerSender.send(event);

    if (!userMessageFlowIds.isEmpty()) {
      try {
        PatternsCS
            .ask(userActor, new MessagesSendMessage.Request(request.getPiId(),
                subjectState.getSsId(), userMessageFlowIds), Global.TIMEOUT)
            .toCompletableFuture().get();
        LOG.info("All users [{}] received the message in PI_ID [{}]", userMessageFlowIds,
            request.getPiId());

        entityManager.refresh(subjectState);
        if (SubjectSubState.SENT.equals(subjectState.getSubState())) {
          changeToNextState(subjectState, request);
        } else {
          sender.tell(new Status.Failure(new IllegalStateException(
              "Sender state is not in 'SENT' state [" + subjectState + "]")), getSelf());
        }
      } catch (final Exception e) {
        sender.tell(
            new Status.Failure(new IllegalStateException(
                "Could not send message to all users in PI_ID [" + request.getPiId() + "]")),
            getSelf());
      }
    }
  }

  private Pair<Long, Long> getUserMessageFlowIds(final Long piId, final MessageFlow messageFlow) {
    final Subject receiver = subjectRepository.getSubjectForSubjectModelInProcess(piId,
        messageFlow.getReceiver().getSmId());
    entityManager.refresh(receiver);
    return Pair.of(receiver.getUser(), messageFlow.getMfId());
  }

  private void triggerSendExternal(final SubjectState subjectState,
      final StateObjectChangeMessage.Request request) {

    final List<MessageFlow> messageFlows = subjectState.getCurrentState().getMessageFlow().stream()
        .filter(mf -> SubjectModelType.EXTERNAL.equals(mf.getReceiver().getSubjectModelType()))
        .collect(Collectors.toList());

    messageFlows.stream()
        .map(mf -> getExternalOutputMessage(request.getPiId(), mf, subjectState.getSubject()))
        .forEachOrdered(output -> {
          LOG.debug("Send message to external-communicator [{}]", output);
          externalCommunicatorClient.sendExternalOutputMessage(output);
          subjectState.setToNotifiedEC();
        });

    if (messageFlows.size() >= 1) {
      if (waitForECResponse(subjectState)) {
        changeToNextState(subjectState, request);
      } else {
        sender.tell(new Status.Failure(new IllegalStateException(
            "Could not send message to all external users in PI_ID [" + request.getPiId() + "]")),
            getSelf());
      }
    }
  }

  private ExternalCommunicatorMessage getExternalOutputMessage(final Long piId,
      final MessageFlow messageFlow, final Subject sender) {
    final Set<BusinessObject> businessObjects = new HashSet<>();

    messageFlow.getBusinessObjectModels().stream().forEachOrdered(bom -> {
      final Set<at.fhjoanneum.ippr.commons.dto.communicator.BusinessObjectField> fields =
          new HashSet<>();
      bom.getBusinessObjectFieldModels().stream().forEachOrdered(field -> {
        final BusinessObjectFieldInstance instance = businessObjectFieldInstanceRepository
            .getBusinessObjectFieldInstanceForModelInProcess(piId, field.getBofmId());
        fields.add(new at.fhjoanneum.ippr.commons.dto.communicator.BusinessObjectField(
            field.getFieldName(), field.getFieldType().name(),
            instance != null ? instance.getValue() : StringUtils.EMPTY));
      });

      businessObjects.add(new BusinessObject(bom.getName(), fields));
    });

    return new ExternalCommunicatorMessage(
        getTransferId(piId, sender.getSId(), messageFlow.getMfId()), businessObjects);
  }


  private boolean waitForECResponse(final SubjectState subjectState) {
    try {
      for (int i = 0; i < 5; i++) {
        TimeUnit.SECONDS.sleep(1);
        entityManager.refresh(subjectState);
        if (SubjectSubState.SENT.equals(subjectState.getSubState())) {
          LOG.debug("Subject is now in 'SENT' [{}]", subjectState);
          return true;
        }
      }
      return false;
    } catch (final Exception e) {
      LOG.error(e.getMessage());
      return false;
    }
  }

  private void changeToNextState(final SubjectState subjectState,
      final StateObjectChangeMessage.Request request) {

    final Long nextStateId = request.getStateObjectChangeDTO().getNextStateId();
    final State nextState = stateRepository.findOne(nextStateId);
    subjectState.setCurrentState(nextState);
    subjectStateRepository.save((SubjectStateImpl) subjectState);
    LOG.info("Changed subject S_ID [{}] to state: {}", subjectState.getSubject().getSId(),
        nextState);

    if (subjectState.getCurrentState().getFunctionType() == StateFunctionType.FUNCTION) {
      final long caseId = subjectState.getProcessInstance().getPiId();
      final long processModelId = subjectState.getProcessInstance().getProcessModel().getPmId();
      final String activity = subjectState.getCurrentState().getName();
      final String timestamp = DateTime.now().toString("dd.MM.yyyy HH:mm");
      final String resource = subjectState.getSubject().getSubjectModel().getName();
      final String state = StateFunctionType.FUNCTION.name();
      final String messageType = "";
      final String recipient = "";
      final String sender = "";

      final EventLoggerDTO event = new EventLoggerDTO(caseId, processModelId, timestamp, activity,
          resource, state, messageType, recipient, sender);
      eventLoggerSender.send(event);
    }
  }


  private void handleAdditionalActions(final SubjectState subjectState) {
    final State currentState = subjectState.getCurrentState();

    if (StateFunctionType.RECEIVE.equals(currentState.getFunctionType())
        && currentState.getTimeoutTransition().isPresent()) {
      startTimeout(subjectState);
    }

    if (StateFunctionType.RECEIVE.equals(currentState.getFunctionType())
        && currentState.getMessageFlow().stream()
            .filter(mf -> mf.getSender().getSubjectModelType().equals(SubjectModelType.EXTERNAL))
            .count() >= 1) {
      notifyExternalCommunicator(subjectState);
    }

    if (StateFunctionType.REFINEMENT.equals(currentState.getFunctionType())) {
      handleRefinement(subjectState);
    }
  }

  private void handleRefinement(final SubjectState subjectState) {
    LOG.info("Special handling since [{}] is a REFINEMENT", subjectState);

    getContext().parent().tell(new ExecuteRefinementMessage.Request(subjectState.getSsId()),
        getSelf());
  }

  private void notifyExternalCommunicator(final SubjectState subjectState) {
    subjectState.getCurrentState().getMessageFlow().stream()
        .filter(mf -> mf.getSender().getSubjectModelType().equals(SubjectModelType.EXTERNAL))
        .map(mf -> getTransferId(subjectState.getProcessInstance().getPiId(),
            subjectState.getSubject().getSId(), mf.getMfId()))
        .collect(Collectors.toSet()).stream().forEachOrdered(transferId -> {
          externalCommunicatorClient.sendReceiveSubmission(new ReceiveSubmissionDTO(transferId));
          LOG.info("Notified external communicator with transferId: {}", transferId);
        });

  }

  private void startTimeout(final SubjectState subjectState) {
    LOG.info("Start timeout for [{}]", subjectState);

    getContext().parent().tell(new TimeoutScheduleStartMessage(subjectState.getSubject().getUser(),
        subjectState.getSsId()), getSelf());
  }

  private static String getTransferId(final Long piId, final Long sId, final Long mfId) {
    final String transferId = piId + "-" + sId + (mfId != null ? "-" + mfId : "");
    return transferId;
  }
}
