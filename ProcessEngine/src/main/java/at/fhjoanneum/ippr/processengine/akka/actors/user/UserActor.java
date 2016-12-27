package at.fhjoanneum.ippr.processengine.akka.actors.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.google.common.collect.Lists;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.pattern.Patterns;
import at.fhjoanneum.ippr.commons.dto.processengine.StateDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.TaskDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.stateobject.BusinessObjectDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.stateobject.BusinessObjectFieldDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.stateobject.BusinessObjectInstanceDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.stateobject.StateObjectDTO;
import at.fhjoanneum.ippr.persistence.entities.engine.businessobject.BusinessObjectInstanceBuilder;
import at.fhjoanneum.ippr.persistence.entities.engine.businessobject.BusinessObjectInstanceImpl;
import at.fhjoanneum.ippr.persistence.entities.engine.businessobject.field.BusinessObjectFieldInstanceBuilder;
import at.fhjoanneum.ippr.persistence.entities.engine.businessobject.field.BusinessObjectFieldInstanceImpl;
import at.fhjoanneum.ippr.persistence.entities.engine.enums.ReceiveSubjectState;
import at.fhjoanneum.ippr.persistence.entities.engine.state.SubjectStateBuilder;
import at.fhjoanneum.ippr.persistence.entities.engine.state.SubjectStateImpl;
import at.fhjoanneum.ippr.persistence.objects.engine.businessobject.BusinessObjectFieldInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.businessobject.BusinessObjectInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.enums.ProcessInstanceState;
import at.fhjoanneum.ippr.persistence.objects.engine.process.ProcessInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.state.SubjectState;
import at.fhjoanneum.ippr.persistence.objects.engine.subject.Subject;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.BusinessObjectModel;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.field.BusinessObjectFieldModel;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.permission.BusinessObjectFieldPermission;
import at.fhjoanneum.ippr.persistence.objects.model.enums.FieldPermission;
import at.fhjoanneum.ippr.persistence.objects.model.enums.FieldType;
import at.fhjoanneum.ippr.persistence.objects.model.enums.StateFunctionType;
import at.fhjoanneum.ippr.persistence.objects.model.state.State;
import at.fhjoanneum.ippr.processengine.akka.config.Global;
import at.fhjoanneum.ippr.processengine.akka.config.SpringExtension;
import at.fhjoanneum.ippr.processengine.akka.messages.EmptyMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.info.TasksOfUserMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.initialize.UserActorInitializeMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.stop.ProcessStopMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.wakeup.UserActorWakeUpMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.workflow.StateObjectChangeMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.workflow.StateObjectMessage;
import at.fhjoanneum.ippr.processengine.parser.DbValueParser;
import at.fhjoanneum.ippr.processengine.repositories.BusinessObjectFieldInstanceRepository;
import at.fhjoanneum.ippr.processengine.repositories.BusinessObjectFieldPermissionRepository;
import at.fhjoanneum.ippr.processengine.repositories.BusinessObjectInstanceRepository;
import at.fhjoanneum.ippr.processengine.repositories.CustomTypesQueriesRepository;
import at.fhjoanneum.ippr.processengine.repositories.ProcessInstanceRepository;
import at.fhjoanneum.ippr.processengine.repositories.StateRepository;
import at.fhjoanneum.ippr.processengine.repositories.SubjectRepository;
import at.fhjoanneum.ippr.processengine.repositories.SubjectStateRepository;
import scala.concurrent.Await;
import scala.concurrent.Future;

@Transactional
@Component("UserActor")
@Scope("prototype")
public class UserActor extends UntypedActor {

  private final static Logger LOG = LoggerFactory.getLogger(UserActor.class);

  @Autowired
  private SpringExtension springExtension;

  @Autowired
  private ProcessInstanceRepository processInstanceRepository;
  @Autowired
  private SubjectRepository subjectRepository;
  @Autowired
  private StateRepository stateRepository;
  @Autowired
  private SubjectStateRepository subjectStateRepository;
  @Autowired
  private CustomTypesQueriesRepository customTypesQueriesRepository;
  @Autowired
  private BusinessObjectFieldPermissionRepository businessObjectFieldPermissionRepository;
  @Autowired
  private BusinessObjectInstanceRepository businessObjectInstanceRepository;
  @Autowired
  private BusinessObjectFieldInstanceRepository businessObjectFieldInstanceRepository;

  @Autowired
  private DbValueParser valueParser;

  private final Long userId;
  private final ActorRef sender;

  public UserActor(final Long userId) {
    this.userId = userId;
    this.sender = getSender();
  }

  @Override
  public void onReceive(final Object obj) throws Throwable {
    if (obj instanceof UserActorInitializeMessage.Request) {
      handleActorInitializeMessage(obj);
    } else if (obj instanceof UserActorWakeUpMessage.Request) {
      handleUserWakeUpMessage(obj);
    } else if (obj instanceof ProcessStopMessage.Request) {
      handleProcessStopMessage(obj);
    } else if (obj instanceof TasksOfUserMessage.Request) {
      handleTasksOfUserMessage(obj);
    } else if (obj instanceof StateObjectMessage.Request) {
      handleStateObjectMessage(obj);
    } else if (obj instanceof StateObjectChangeMessage.Request) {
      handleStateObjectChangeMessage(obj);
    } else {
      unhandled(obj);
    }
  }

  private void handleActorInitializeMessage(final Object obj) {
    final UserActorInitializeMessage.Request msg = (UserActorInitializeMessage.Request) obj;

    final ProcessInstance processInstance =
        Optional.ofNullable(processInstanceRepository.findOne(msg.getPiId())).get();

    final Subject subject = Optional.ofNullable(subjectRepository.findOne(msg.getSId())).get();

    final State state = Optional
        .ofNullable(stateRepository.getStartStateOfSubject(subject.getSubjectModel().getSmId()))
        .get();

    ReceiveSubjectState receiveSubjectState = null;
    if (state.getFunctionType().equals(StateFunctionType.RECEIVE)) {
      receiveSubjectState = ReceiveSubjectState.TO_RECEIVE;
    }

    final SubjectState subjectState = new SubjectStateBuilder().processInstance(processInstance)
        .subject(subject).state(state).receiveSubjectState(receiveSubjectState).build();

    subjectStateRepository.save((SubjectStateImpl) subjectState);
    LOG.info("Subject is now in initial state: {}", subjectState);

    TransactionSynchronizationManager
        .registerSynchronization(new TransactionSynchronizationAdapter() {
          @Override
          public void afterCommit() {
            // notify user supervisor actor
            sender.tell(new EmptyMessage(), getSelf());
          }
        });

    if (userId.longValue() == Global.DESTROY_ID.longValue()) {
      getContext().stop(getSelf());
    }
  }

  private void handleUserWakeUpMessage(final Object obj) {
    final UserActorWakeUpMessage.Request msg = (UserActorWakeUpMessage.Request) obj;
    getSender().tell(new UserActorWakeUpMessage.Request(msg.getUserId()), getSelf());
  }

  private void handleProcessStopMessage(final Object obj) {
    final Long amountOfActiveProcesses = processInstanceRepository
        .getAmountOfProcessesPerUser(ProcessInstanceState.ACTIVE.name(), userId);

    if (amountOfActiveProcesses.longValue() == 0) {
      getContext().stop(getSelf());
      LOG.info("No active processes anymore for user [{}], therefore shutdown actor",
          getSelf().path().name());
    }
  }

  private void handleTasksOfUserMessage(final Object obj) {
    final TasksOfUserMessage.Request msg = (TasksOfUserMessage.Request) obj;

    final List<TaskDTO> tasks = customTypesQueriesRepository.getTasksOfUser(msg.getUserId());
    getSender().tell(new TasksOfUserMessage.Response(tasks), getSelf());
  }

  private void handleStateObjectMessage(final Object obj) {
    final StateObjectMessage.Request request = (StateObjectMessage.Request) obj;

    final SubjectState subjectState =
        Optional
            .ofNullable(subjectStateRepository
                .getSubjectStateOfUserInProcessInstance(request.getPiId(), request.getUserId()))
            .get();

    final List<BusinessObjectDTO> businessObjects = Lists.newArrayList();

    for (final BusinessObjectModel businessObjectModel : subjectState.getCurrentState()
        .getBusinessObjectModels()) {

      LOG.debug("Found business object model: {}", businessObjectModel);

      final List<BusinessObjectFieldDTO> fields = Lists.newArrayList();
      final Optional<BusinessObjectInstance> businessObjectInstanceOpt = Optional
          .ofNullable(businessObjectInstanceRepository.getBusinessObjectInstanceOfModelInProcess(
              request.getPiId(), businessObjectModel.getBomId()));

      if (!businessObjectInstanceOpt.isPresent()) {
        LOG.debug("No business object instance is present for BOM_ID [{}] in process PI_ID [{}]",
            businessObjectModel.getBomId(), request.getPiId());
      }

      for (final BusinessObjectFieldModel businessObjectFieldModel : businessObjectModel
          .getBusinessObjectFieldModels()) {
        final BusinessObjectFieldPermission businessObjectFieldPermission =
            businessObjectFieldPermissionRepository.getBusinessObjectFieldPermissionInState(
                businessObjectFieldModel.getBofmId(), subjectState.getCurrentState().getSId());

        if (!businessObjectFieldPermission.getPermission().equals(FieldPermission.NONE)) {
          final Long bofmId = businessObjectFieldModel.getBofmId();
          final String name = businessObjectFieldModel.getFieldName();
          final String type = businessObjectFieldModel.getFieldType().name();
          final boolean required = businessObjectFieldPermission.isMandatory();
          final boolean readOnly =
              businessObjectFieldPermission.getPermission().equals(FieldPermission.READ) ? true
                  : false;

          String value = null;
          Long bofiId = null;
          if (businessObjectInstanceOpt.isPresent()) {
            final Optional<BusinessObjectFieldInstance> fieldInstanceOpt = businessObjectInstanceOpt
                .get().getBusinessObjectFieldInstanceOfFieldModel(businessObjectFieldModel);

            if (fieldInstanceOpt.isPresent()) {
              bofiId = fieldInstanceOpt.get().getBofiId();

              // TODO add casting
              value = fieldInstanceOpt.get().getValue();
            }
          }
          fields.add(
              new BusinessObjectFieldDTO(bofmId, bofiId, name, type, required, readOnly, value));
        } else {
          LOG.debug("Not necessary to add field [{}] since permission is 'NONE'");
          continue;
        }
      }
      businessObjects.add(new BusinessObjectDTO(businessObjectModel.getBomId(),
          businessObjectInstanceOpt.isPresent() ? businessObjectInstanceOpt.get().getBoiId() : null,
          businessObjectModel.getName(), fields));
    }

    final List<StateDTO> nextStates = subjectState.getCurrentState().getToStates().stream()
        .map(state -> new StateDTO(state.getToState().getSId(), state.getToState().getName()))
        .collect(Collectors.toList());

    getSender().tell(new StateObjectMessage.Response(
        new StateObjectDTO(request.getPiId(), subjectState.getSsId(), businessObjects, nextStates)),
        getSelf());
  }

  private void handleStateObjectChangeMessage(final Object obj) throws Exception {
    final StateObjectChangeMessage.Request request = (StateObjectChangeMessage.Request) obj;

    final SubjectState subjectState =
        Optional
            .ofNullable(subjectStateRepository
                .getSubjectStateOfUserInProcessInstance(request.getPiId(), request.getUserId()))
            .get();

    final ActorRef sender = getSender();

    final ActorRef bussinessObjectCheckActor = getContext().actorOf(
        springExtension.props("BusinessObjectCheckActor", subjectState.getCurrentState().getSId()),
        UUID.randomUUID().toString());

    // must block thread since transaction get lost when using completable future
    final Future<Object> future = Patterns.ask(bussinessObjectCheckActor, request, Global.TIMEOUT);
    final boolean correct =
        ((Boolean) Await.result(future, Global.TIMEOUT.duration())).booleanValue();

    if (!correct) {
      sender.tell(new akka.actor.Status.Failure(
          new IllegalArgumentException("Check of business objects returned false")), getSelf());
    } else {
      initBusinessObjectInstances(subjectState, request);
      setValuesOfBusinessObjectFieldInstances(subjectState.getCurrentState(), request);
      changeToNextState(subjectState, request);

      TransactionSynchronizationManager
          .registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
              sender.tell(new EmptyMessage(), getSelf());
            }
          });
    }
  }

  private void initBusinessObjectInstances(final SubjectState state,
      final StateObjectChangeMessage.Request request) {
    final ProcessInstance processInstance = processInstanceRepository.findOne(request.getPiId());

    final List<BusinessObjectModel> toCreate = state.getCurrentState().getBusinessObjectModels()
        .stream().filter(model -> !processInstance.isBusinessObjectInstanceOfModelCreated(model))
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
    request.getStateObjectChangeDTO().getBusinessObjects().stream()
        .map(BusinessObjectInstanceDTO::getFields).flatMap(List::stream).forEach(field -> {
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

  private void changeToNextState(final SubjectState subjectState,
      final StateObjectChangeMessage.Request request) {
    final Long nextStateId = request.getStateObjectChangeDTO().getNextStateId();
    final State nextState = stateRepository.findOne(nextStateId);
    subjectState.setCurrentState(nextState);
    subjectStateRepository.save((SubjectStateImpl) subjectState);
    LOG.info("Changed subject S_ID [{}] to state: {}", subjectState.getSubject().getSId(),
        nextState);
  }
}
