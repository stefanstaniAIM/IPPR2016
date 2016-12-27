package at.fhjoanneum.ippr.processengine.akka.actors.process;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.google.common.collect.Lists;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import at.fhjoanneum.ippr.commons.dto.processengine.ProcessInfoDTO;
import at.fhjoanneum.ippr.persistence.entities.engine.process.ProcessInstanceBuilder;
import at.fhjoanneum.ippr.persistence.entities.engine.process.ProcessInstanceImpl;
import at.fhjoanneum.ippr.persistence.entities.engine.subject.SubjectBuilder;
import at.fhjoanneum.ippr.persistence.entities.engine.subject.SubjectImpl;
import at.fhjoanneum.ippr.persistence.objects.engine.enums.ProcessInstanceState;
import at.fhjoanneum.ippr.persistence.objects.engine.process.ProcessInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.subject.Subject;
import at.fhjoanneum.ippr.persistence.objects.model.process.ProcessModel;
import at.fhjoanneum.ippr.persistence.objects.model.subject.SubjectModel;
import at.fhjoanneum.ippr.processengine.akka.AkkaSelector;
import at.fhjoanneum.ippr.processengine.akka.config.SpringExtension;
import at.fhjoanneum.ippr.processengine.akka.messages.process.info.ProcessInfoMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.info.ProcessStateMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.initialize.ProcessStartMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.initialize.ProcessStartMessage.UserGroupAssignment;
import at.fhjoanneum.ippr.processengine.akka.messages.process.stop.ProcessStopMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.wakeup.ProcessWakeUpMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.workflow.StateObjectChangeMessage;
import at.fhjoanneum.ippr.processengine.repositories.ProcessInstanceRepository;
import at.fhjoanneum.ippr.processengine.repositories.ProcessModelRepository;
import at.fhjoanneum.ippr.processengine.repositories.SubjectModelRepository;
import at.fhjoanneum.ippr.processengine.repositories.SubjectRepository;

@Transactional
@Component("ProcessSupervisorActor")
@Scope("prototype")
public class ProcessSupervisorActor extends UntypedActor {

  private final static Logger LOG = LoggerFactory.getLogger(ProcessSupervisorActor.class);

  @Autowired
  private AkkaSelector akkaSelector;

  @Autowired
  private ProcessModelRepository processModelRepository;
  @Autowired
  private SubjectModelRepository subjectModelRepository;
  @Autowired
  private ProcessInstanceRepository processInstanceRepository;
  @Autowired
  private SubjectRepository subjectRepository;
  @Autowired
  private SpringExtension springExtension;

  @Override
  public void onReceive(final Object obj) throws Throwable {
    if (obj instanceof ProcessStartMessage.Request) {
      LOG.info("Received {} and create actor for it", obj);
      handleProcessStartMessage(obj);
    } else if (obj instanceof ProcessStateMessage.Request) {
      LOG.info("Received {} and will create state repsonse", obj);
      handleProcessStateMessage(obj);
    } else if (obj instanceof ProcessWakeUpMessage.Request) {
      handleProcessWakeUpMessage(obj);
    } else if (obj instanceof ProcessInfoMessage.Request) {
      handleProcessInfoMessage(obj);
    } else if (obj instanceof ProcessStopMessage.Request) {
      handleProcessStopMessage(obj);
    } else if (obj instanceof StateObjectChangeMessage.Request) {
      handleStateObjectChangeMessage(obj);
    } else {
      unhandled(obj);
    }
  }

  private void handleProcessStartMessage(final Object obj) {
    try {
      final ProcessStartMessage.Request msg = (ProcessStartMessage.Request) obj;

      LOG.info("Handle ProcessStartMessage and will create new process instance");
      final ProcessInstanceBuilder processBuilder = new ProcessInstanceBuilder();

      final Optional<ProcessModel> processModel =
          Optional.ofNullable(processModelRepository.findOne(msg.getPmId()));
      if (!processModel.isPresent()) {
        throw new IllegalStateException("Could not find process model");
      }
      processBuilder.processModel(processModel.get());

      final SubjectModel starterSubjectModel = processModel.get().getStarterSubjectModel();
      final List<UserGroupAssignment> assignments = Lists.newArrayList(
          new UserGroupAssignment(starterSubjectModel.getSmId(), msg.getStartUserId(), null));

      assignments.addAll(processModel.get().getSubjectModels().stream()
          .filter(subjectModel -> !subjectModel.getSmId().equals(starterSubjectModel.getSmId()))
          .map(subjectModel -> new UserGroupAssignment(subjectModel.getSmId(), null, null))
          .collect(Collectors.toList()));

      assignments.stream().map(entry -> createSubject(processBuilder, entry))
          .forEach(subject -> subjectRepository.save((SubjectImpl) subject));

      processBuilder.startUserId(msg.getStartUserId());

      final ProcessInstance processInstance =
          processInstanceRepository.save((ProcessInstanceImpl) processBuilder.build());
      LOG.info("Created new process instance: {}", processInstance);

      final String processInstanceId = getProcessActorId(processInstance.getPiId());
      final Optional<ActorRef> actorOpt = akkaSelector.findActor(getContext(), processInstanceId);
      if (!actorOpt.isPresent()) {
        getContext().actorOf(springExtension.props("ProcessActor", processInstance.getPiId()),
            processInstanceId);
        LOG.info("Created new actor for process instance: {}", processInstanceId);

        TransactionSynchronizationManager
            .registerSynchronization(new TransactionSynchronizationAdapter() {
              @Override
              public void afterCommit() {
                getSender().tell(new ProcessStartMessage.Response(processInstance.getPiId()),
                    getSelf());
              }
            });
      }
    } catch (final Exception e) {
      getSender().tell(new akka.actor.Status.Failure(e), getSelf());
    }
  }

  private String getProcessActorId(final Long piId) {
    return "Process-" + piId;
  }

  private Subject createSubject(final ProcessInstanceBuilder processBuilder,
      final UserGroupAssignment entry) throws IllegalStateException {
    final Optional<SubjectModel> subjectModel =
        Optional.ofNullable(subjectModelRepository.findOne(entry.getSmId()));
    if (!subjectModel.isPresent()) {
      throw new IllegalStateException("Could not find subject model");
    }

    final SubjectBuilder builder = new SubjectBuilder().subjectModel(subjectModel.get());
    if (entry.getUserId() != null) {
      builder.userId(entry.getUserId());
    } else if (entry.getGroupId() != null) {
      builder.groupId(entry.getGroupId());
    }

    final Subject subject = builder.build();
    processBuilder.addSubject(subject);
    return subject;
  }

  private void handleProcessStateMessage(final Object obj) {
    final ProcessStateMessage.Request msg = (ProcessStateMessage.Request) obj;

    final String processInstanceId = getProcessActorId(msg.getPiId());
    final Optional<ActorRef> actorOpt = akkaSelector.findActor(getContext(), processInstanceId);

    if (!actorOpt.isPresent()) {
      final String error = "Could not find process actor for: " + processInstanceId;
      getSender().tell(new akka.actor.Status.Failure(new IllegalArgumentException(error)),
          getSelf());
      return;
    }

    actorOpt.get().forward(msg, getContext());
  }

  private void handleProcessWakeUpMessage(final Object obj) {
    final ProcessWakeUpMessage.Request msg = (ProcessWakeUpMessage.Request) obj;

    final String processInstanceId = getProcessActorId(msg.getPiId());
    final Optional<ActorRef> actorOpt = akkaSelector.findActor(getContext(), processInstanceId);

    if (!actorOpt.isPresent()) {
      getContext().actorOf(springExtension.props("ProcessActor", msg.getPiId()), processInstanceId);
      LOG.info("Created new actor for process instance after wake up message: {}",
          processInstanceId);
    }

    getSender().tell(new ProcessWakeUpMessage.Response(msg.getPiId()), getSelf());
  }

  private void handleProcessInfoMessage(final Object obj) {
    final ProcessInfoMessage.Request msg = (ProcessInfoMessage.Request) obj;

    final PageRequest pageRequest =
        new PageRequest(msg.getPage(), msg.getSize(), new Sort(Sort.Direction.DESC, "startTime"));


    List<ProcessInstance> content = null;
    if (msg.getUser() == null) {
      LOG.debug("Received ProcessInfoMessage to show all processes");
      content = Lists.newArrayList(processInstanceRepository
          .getProcessesInfoOfState(pageRequest, ProcessInstanceState.valueOf(msg.getState()))
          .getContent());
    } else {
      LOG.debug("Received ProcessInfoMessage to show all processes of involved user [{}]",
          msg.getUser());
      content =
          Lists.newArrayList(processInstanceRepository.getProcessesInfoOfUserAndState(pageRequest,
              msg.getUser(), ProcessInstanceState.valueOf(msg.getState())).getContent());
    }

    final List<ProcessInfoDTO> processesInfo = content.stream().map(process -> {
      final String processName = process.getProcessModel().getName();
      return new ProcessInfoDTO(process.getPiId(), process.getStartTime(), process.getEndTime(),
          processName, process.getStartUserId());
    }).collect(Collectors.toList());

    getSender().tell(new ProcessInfoMessage.Response(processesInfo), getSelf());
  }

  private void handleProcessStopMessage(final Object obj) {
    final ProcessStopMessage.Request msg = (ProcessStopMessage.Request) obj;
    forwardToProcessActor(msg.getPiId(), msg);
  }

  private <T> void forwardToProcessActor(final Long piId, final T msg) {
    final String processInstanceId = getProcessActorId(piId);
    final Optional<ActorRef> actorOpt = akkaSelector.findActor(getContext(), processInstanceId);

    if (actorOpt.isPresent()) {
      LOG.debug("Found process actor and will forward message to stop process");
      actorOpt.get().forward(msg, getContext());
    }
  }

  private void handleStateObjectChangeMessage(final Object obj) {
    final StateObjectChangeMessage.Request request = (StateObjectChangeMessage.Request) obj;
    forwardToProcessActor(request.getPiId(), request);
  }
}
