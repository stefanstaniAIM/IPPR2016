package at.fhjoanneum.ippr.processengine.akka.tasks.process;

import akka.actor.ActorRef;
import at.fhjoanneum.ippr.commons.dto.eventlogger.EventLoggerDTO;
import at.fhjoanneum.ippr.persistence.entities.engine.process.ProcessInstanceBuilder;
import at.fhjoanneum.ippr.persistence.entities.engine.process.ProcessInstanceImpl;
import at.fhjoanneum.ippr.persistence.entities.engine.subject.SubjectBuilder;
import at.fhjoanneum.ippr.persistence.entities.engine.subject.SubjectImpl;
import at.fhjoanneum.ippr.persistence.objects.engine.process.ProcessInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.subject.Subject;
import at.fhjoanneum.ippr.persistence.objects.model.process.ProcessModel;
import at.fhjoanneum.ippr.persistence.objects.model.subject.SubjectModel;
import at.fhjoanneum.ippr.processengine.akka.messages.process.initialize.ProcessStartMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.initialize.ProcessStartMessage.UserGroupAssignment;
import at.fhjoanneum.ippr.processengine.akka.tasks.AbstractTask;
import at.fhjoanneum.ippr.processengine.akka.tasks.TaskCallback;
import at.fhjoanneum.ippr.processengine.repositories.ProcessInstanceRepository;
import at.fhjoanneum.ippr.processengine.repositories.ProcessModelRepository;
import at.fhjoanneum.ippr.processengine.repositories.SubjectModelRepository;
import at.fhjoanneum.ippr.processengine.repositories.SubjectRepository;
import at.fhjoanneum.ippr.processengine.services.EventLoggerSender;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component("ProcessSupervisor.ProcessStartTask")
@Scope("prototype")
public class ProcessStartTask extends AbstractTask<ProcessStartMessage.Request> {

  private final static Logger LOG = LoggerFactory.getLogger(ProcessStartTask.class);

  @Autowired
  private ProcessModelRepository processModelRepository;
  @Autowired
  private SubjectRepository subjectRepository;
  @Autowired
  private SubjectModelRepository subjectModelRepository;
  @Autowired
  private ProcessInstanceRepository processInstanceRepository;
  @Autowired
  private EventLoggerSender eventLoggerSender;

  public <T> ProcessStartTask(final TaskCallback<T> callback) {
    super(callback);
  }

  @Override
  public boolean canHandle(final Object obj) {
    return obj instanceof ProcessStartMessage.Request;
  }

  @Override
  public void execute(final ProcessStartMessage.Request msg) throws Exception {
    try {
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
      callback(processInstance.getPiId());

      final ActorRef sender = getSender();
      TransactionSynchronizationManager
          .registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
              // start process
              sender.tell(new ProcessStartMessage.Response(processInstance.getPiId()), getSelf());
              final long caseId = processInstance.getPiId();
              final long processModelId = processInstance.getProcessModel().getPmId();
              final String activity = "Process Start";
              final String timestamp = DateTime.now().toString("dd.MM.yyyy HH:mm");
              final EventLoggerDTO event =
                  new EventLoggerDTO(caseId, processModelId, timestamp, activity, "", "", "", "", "");
              eventLoggerSender.send(event);
            }
          });
    } catch (final Exception e) {
      getSender().tell(new akka.actor.Status.Failure(e), getSelf());
    }
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
}
