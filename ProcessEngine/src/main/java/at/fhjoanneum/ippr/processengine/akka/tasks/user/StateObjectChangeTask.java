package at.fhjoanneum.ippr.processengine.akka.tasks.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import akka.actor.ActorRef;
import akka.actor.Status;
import akka.pattern.Patterns;
import akka.pattern.PatternsCS;
import at.fhjoanneum.ippr.commons.dto.processengine.stateobject.BusinessObjectInstanceDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.stateobject.UserAssignmentDTO;
import at.fhjoanneum.ippr.persistence.entities.engine.businessobject.BusinessObjectInstanceBuilder;
import at.fhjoanneum.ippr.persistence.entities.engine.businessobject.BusinessObjectInstanceImpl;
import at.fhjoanneum.ippr.persistence.entities.engine.businessobject.field.BusinessObjectFieldInstanceBuilder;
import at.fhjoanneum.ippr.persistence.entities.engine.businessobject.field.BusinessObjectFieldInstanceImpl;
import at.fhjoanneum.ippr.persistence.entities.engine.enums.SubjectSubState;
import at.fhjoanneum.ippr.persistence.entities.engine.state.SubjectStateImpl;
import at.fhjoanneum.ippr.persistence.entities.engine.subject.SubjectImpl;
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
import at.fhjoanneum.ippr.persistence.objects.model.state.State;
import at.fhjoanneum.ippr.processengine.akka.config.Global;
import at.fhjoanneum.ippr.processengine.akka.config.SpringExtension;
import at.fhjoanneum.ippr.processengine.akka.messages.EmptyMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.workflow.MessagesSendMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.workflow.StateObjectChangeMessage;
import at.fhjoanneum.ippr.processengine.akka.tasks.AbstractTask;
import at.fhjoanneum.ippr.processengine.parser.DbValueParser;
import at.fhjoanneum.ippr.processengine.repositories.BusinessObjectFieldInstanceRepository;
import at.fhjoanneum.ippr.processengine.repositories.BusinessObjectFieldPermissionRepository;
import at.fhjoanneum.ippr.processengine.repositories.BusinessObjectInstanceRepository;
import at.fhjoanneum.ippr.processengine.repositories.ProcessInstanceRepository;
import at.fhjoanneum.ippr.processengine.repositories.StateRepository;
import at.fhjoanneum.ippr.processengine.repositories.SubjectRepository;
import at.fhjoanneum.ippr.processengine.repositories.SubjectStateRepository;
import scala.concurrent.Await;
import scala.concurrent.Future;

@Component("User.StateObjectChangeTask")
@Scope("prototype")
public class StateObjectChangeTask extends AbstractTask<StateObjectChangeMessage.Request> {

  private final static Logger LOG = LoggerFactory.getLogger(StateObjectChangeTask.class);

  @Autowired
  private SpringExtension springExtension;
  @Autowired
  private ProcessInstanceRepository processInstanceRepository;
  @Autowired
  private SubjectRepository subjectRepository;
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
    final ActorRef sender = getSender();

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

  private void sendMessages(final SubjectState subjectState,
      final StateObjectChangeMessage.Request request) {
    if (StateFunctionType.SEND.equals(subjectState.getCurrentState().getFunctionType())
        && SubjectSubState.TO_SEND.equals(subjectState.getSubState())) {
      assignUsers(request);
      triggerSend(subjectState, request);
    } else {
      changeToNextState(subjectState, request);
    }
  }

  private void assignUsers(final StateObjectChangeMessage.Request request) {
    request.getStateObjectChangeDTO().getUserAssignments().forEach(assignment -> {
      final Subject subject = subjectRepository
          .getSubjectForSubjectModelInProcess(request.getPiId(), assignment.getSmId());
      subject.setUser(assignment.getUserId());
      subjectRepository.save((SubjectImpl) subject);
      LOG.info("New user for subject: {}", subject);
    });
  }

  private void triggerSend(final SubjectState subjectState,
      final StateObjectChangeMessage.Request request) {
    // after commit to ensure that user assignment is done
    TransactionSynchronizationManager
        .registerSynchronization(new TransactionSynchronizationAdapter() {
          @Override
          public void afterCommit() {
            final ActorRef userActor = getContext().parent();
            final List<Long> userIds = request.getStateObjectChangeDTO().getUserAssignments()
                .stream().map(UserAssignmentDTO::getUserId).collect(Collectors.toList());

            try {
              PatternsCS.ask(userActor, new MessagesSendMessage.Request(request.getPiId(),
                  subjectState.getSsId(), userIds), Global.TIMEOUT).toCompletableFuture().get();
              LOG.info("All users [{}] received the message in PI_ID [{}]", userIds,
                  request.getPiId());
              entityManager.refresh(subjectState);
              if (SubjectSubState.SENT.equals(subjectState.getSubState())) {
                changeToNextState(subjectState, request);
              } else {
                sender
                    .tell(
                        new Status.Failure(new IllegalStateException(
                            "Sender state is not in 'SENT' state [" + subjectState + "]")),
                        getSelf());
              }
            } catch (final Exception e) {
              sender.tell(
                  new Status.Failure(new IllegalStateException(
                      "Could not send message to all users in PI_ID [" + request.getPiId() + "]")),
                  getSelf());
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
