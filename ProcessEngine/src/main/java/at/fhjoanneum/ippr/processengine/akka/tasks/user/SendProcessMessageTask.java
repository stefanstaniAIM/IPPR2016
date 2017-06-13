package at.fhjoanneum.ippr.processengine.akka.tasks.user;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import akka.actor.ActorRef;
import akka.pattern.PatternsCS;
import at.fhjoanneum.ippr.commons.dto.processengine.SendProcessMessage;
import at.fhjoanneum.ippr.persistence.entities.engine.businessobject.BusinessObjectInstanceBuilder;
import at.fhjoanneum.ippr.persistence.entities.engine.businessobject.BusinessObjectInstanceImpl;
import at.fhjoanneum.ippr.persistence.entities.engine.businessobject.field.BusinessObjectFieldInstanceBuilder;
import at.fhjoanneum.ippr.persistence.entities.engine.businessobject.field.BusinessObjectFieldInstanceImpl;
import at.fhjoanneum.ippr.persistence.entities.engine.process.ProcessInstanceBuilder;
import at.fhjoanneum.ippr.persistence.entities.engine.process.ProcessInstanceImpl;
import at.fhjoanneum.ippr.persistence.entities.engine.state.SubjectStateBuilder;
import at.fhjoanneum.ippr.persistence.entities.engine.state.SubjectStateImpl;
import at.fhjoanneum.ippr.persistence.entities.engine.subject.SubjectBuilder;
import at.fhjoanneum.ippr.persistence.entities.engine.subject.SubjectImpl;
import at.fhjoanneum.ippr.persistence.objects.engine.businessobject.BusinessObjectFieldInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.businessobject.BusinessObjectInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.process.ProcessInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.state.SubjectState;
import at.fhjoanneum.ippr.persistence.objects.engine.subject.Subject;
import at.fhjoanneum.ippr.persistence.objects.model.enums.SubjectModelType;
import at.fhjoanneum.ippr.persistence.objects.model.messageflow.MessageFlow;
import at.fhjoanneum.ippr.persistence.objects.model.process.ProcessModel;
import at.fhjoanneum.ippr.persistence.objects.model.state.State;
import at.fhjoanneum.ippr.processengine.akka.config.Global;
import at.fhjoanneum.ippr.processengine.akka.messages.process.wakeup.ProcessWakeUpMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.wakeup.UserActorWakeUpMessage;
import at.fhjoanneum.ippr.processengine.akka.tasks.AbstractTask;
import at.fhjoanneum.ippr.processengine.repositories.BusinessObjectFieldInstanceRepository;
import at.fhjoanneum.ippr.processengine.repositories.BusinessObjectInstanceRepository;
import at.fhjoanneum.ippr.processengine.repositories.MessageFlowRepository;
import at.fhjoanneum.ippr.processengine.repositories.ProcessInstanceRepository;
import at.fhjoanneum.ippr.processengine.repositories.ProcessModelRepository;
import at.fhjoanneum.ippr.processengine.repositories.StateRepository;
import at.fhjoanneum.ippr.processengine.repositories.SubjectRepository;
import at.fhjoanneum.ippr.processengine.repositories.SubjectStateRepository;

@Component("User.SendProcessMessageTask")
@Scope("prototype")
public class SendProcessMessageTask extends AbstractTask<SendProcessMessage.Request> {

  private final static Logger LOG = LoggerFactory.getLogger(SendProcessMessageTask.class);

  @Autowired
  private ActorRef processSupervisorActor;

  @Autowired
  private ActorRef userSupervisorActor;

  @Autowired
  private ProcessModelRepository processModelRepository;
  @Autowired
  private SubjectRepository subjectRepository;
  @Autowired
  private ProcessInstanceRepository processInstanceRepository;
  @Autowired
  private MessageFlowRepository messageFlowRepository;
  @Autowired
  private BusinessObjectInstanceRepository businessObjectInstanceRepository;
  @Autowired
  private BusinessObjectFieldInstanceRepository businessObjectFieldInstanceRepository;
  @Autowired
  private StateRepository stateRepository;
  @Autowired
  private SubjectStateRepository subjectStateRepository;

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public boolean canHandle(final Object obj) {
    return obj instanceof SendProcessMessage.Request;
  }

  @Override
  public void execute(final SendProcessMessage.Request message) throws Exception {
    final MessageFlow sendMf = messageFlowRepository.findOne(message.getMfId());
    final Subject senderSubject = subjectRepository.findOne(message.getSubjectPartnerId());

    if (!senderSubject.getProcessPartner().isPresent()) {
      final ProcessInstance processInstance = startProcess(sendMf, message.getUserId());
      final Subject receiveSubject = initSubjects(processInstance);
      initBusinessObjects(processInstance, sendMf, receiveSubject, message.getBoId());

      PatternsCS.ask(processSupervisorActor,
          new ProcessWakeUpMessage.Request(processInstance.getPiId()), Global.TIMEOUT)
          .toCompletableFuture().get();

      PatternsCS.ask(userSupervisorActor,
          new UserActorWakeUpMessage.Request(receiveSubject.getUser()), Global.TIMEOUT)
          .toCompletableFuture().get();

      receiveSubject.setProcessPartner(senderSubject);
    } else {
      final Subject receiveSubject = senderSubject.getProcessPartner().get();
      initBusinessObjects(receiveSubject.getSubjectState().getProcessInstance(), sendMf,
          receiveSubject, message.getBoId());
      receiveSubject.setProcessPartner(senderSubject);
    }

    final ActorRef sender = getSender();

    TransactionSynchronizationManager
        .registerSynchronization(new TransactionSynchronizationAdapter() {
          @Override
          public void afterCommit() {
            sender.tell(new SendProcessMessage.Response(), getSelf());
          }
        });
  }

  private ProcessInstance startProcess(final MessageFlow mf, final Long userId) {
    final ProcessInstanceBuilder processBuilder = new ProcessInstanceBuilder();
    final ProcessModel processModel =
        processModelRepository.findOne(mf.getAssignedProcessModel().get().getPmId());
    processBuilder.processModel(processModel);
    processBuilder.startUserId(userId);

    processModel.getSubjectModels().stream()
        .filter(sm -> SubjectModelType.INTERNAL.equals(sm.getSubjectModelType())).forEach(sm -> {
          final Subject subject = new SubjectBuilder().subjectModel(sm).build();
          if (processModel.getStarterSubjectModel().equals(sm)) {
            subject.setUser(userId);
          }

          subjectRepository.save((SubjectImpl) subject);
          processBuilder.addSubject(subject);
          LOG.debug("Saved new subject [{}]", subject);
        });

    final ProcessInstanceImpl process = (ProcessInstanceImpl) processBuilder.build();
    processInstanceRepository.save(process);
    LOG.debug("Saved new process [{}]", process);

    return process;
  }

  private Subject initSubjects(final ProcessInstance processInstance) {
    // subject that receives message
    Subject startSubject = null;

    for (final Subject subject : processInstance.getSubjects()) {
      if (SubjectModelType.INTERNAL.equals(subject.getSubjectModel().getSubjectModelType())) {
        final State state = Optional
            .ofNullable(stateRepository.getStartStateOfSubject(subject.getSubjectModel().getSmId()))
            .get();

        final SubjectState subjectState = new SubjectStateBuilder().processInstance(processInstance)
            .subject(subject).state(state).build();

        subjectStateRepository.save((SubjectStateImpl) subjectState);
        LOG.debug("Subject is now in initial state: {}", subjectState);

        if (subject.getUser() != null) {
          entityManager.refresh(subject);
          startSubject = subject;
        }
      }
    }
    return startSubject;
  }

  private void initBusinessObjects(final ProcessInstance process, final MessageFlow sendMf,
      final Subject startSubject, final Long boId) {
    if (sendMf == null) {
      throw new IllegalArgumentException();
    }

    MessageFlow receiveMf = null;
    for (final MessageFlow mf : startSubject.getSubjectState().getCurrentState().getMessageFlow()) {
      if (sendMf.getBusinessObjectModels().get(0).getName()
          .equals(mf.getBusinessObjectModels().get(0).getName())) {
        LOG.debug("Found appropiate message flow [{}]", mf);
        receiveMf = mf;
      }
    }

    final BusinessObjectInstance boToCopy = businessObjectInstanceRepository.findOne(boId);
    final BusinessObjectInstance newBo =
        new BusinessObjectInstanceBuilder().processInstance(process)
            .businessObjectModel(receiveMf.getBusinessObjectModels().get(0)).build();
    businessObjectInstanceRepository.save((BusinessObjectInstanceImpl) newBo);

    final Map<String, BusinessObjectFieldInstance> fields =
        getFieldMap(boToCopy.getBusinessObjectFieldInstances());

    newBo.getBusinessObjectModel().getBusinessObjectFieldModels().forEach(field -> {
      final BusinessObjectFieldInstance newField = new BusinessObjectFieldInstanceBuilder()
          .businessObjectFieldModel(field).businessObjectInstance(newBo)
          .value(fields.get(field.getFieldName()).getValue()).build();
      businessObjectFieldInstanceRepository.save((BusinessObjectFieldInstanceImpl) newField);
    });

    startSubject.getSubjectState().setToReceived(receiveMf);
  }

  private static Map<String, BusinessObjectFieldInstance> getFieldMap(
      final List<BusinessObjectFieldInstance> fields) {
    return fields.stream()
        .collect(Collectors.toMap(f -> f.getBusinessObjectFieldModel().getFieldName(), f -> f));
  }
}
