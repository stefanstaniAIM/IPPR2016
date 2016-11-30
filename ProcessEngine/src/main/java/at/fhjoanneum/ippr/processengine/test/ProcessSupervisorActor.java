package at.fhjoanneum.ippr.processengine.test;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import at.fhjoanneum.ippr.persistence.entities.engine.process.ProcessInstanceBuilder;
import at.fhjoanneum.ippr.persistence.entities.engine.process.ProcessInstanceImpl;
import at.fhjoanneum.ippr.persistence.entities.engine.subject.SubjectBuilder;
import at.fhjoanneum.ippr.persistence.entities.engine.subject.SubjectImpl;
import at.fhjoanneum.ippr.persistence.objects.engine.process.ProcessInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.subject.Subject;
import at.fhjoanneum.ippr.persistence.objects.model.process.ProcessModel;
import at.fhjoanneum.ippr.persistence.objects.model.subject.SubjectModel;
import at.fhjoanneum.ippr.processengine.akka.AkkaSelector;
import at.fhjoanneum.ippr.processengine.akka.config.SpringExtension;
import at.fhjoanneum.ippr.processengine.akka.messages.process.ProcessStartMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.ProcessStartMessage.UserGroupAssignment;
import at.fhjoanneum.ippr.processengine.repositories.ProcessInstanceRepository;
import at.fhjoanneum.ippr.processengine.repositories.ProcessModelRepository;
import at.fhjoanneum.ippr.processengine.repositories.SubjectModelRepository;
import at.fhjoanneum.ippr.processengine.repositories.SubjectRepository;

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
      final ProcessStartMessage.Request msg = (ProcessStartMessage.Request) obj;
      handleProcessStartMessage(msg);
    } else {
      unhandled(obj);
    }
  }

  private void handleProcessStartMessage(final ProcessStartMessage.Request msg) {
    try {
      LOG.info("Handle ProcessStartMessage and will create new process instance");
      final ProcessInstanceBuilder processBuilder = new ProcessInstanceBuilder();

      final Optional<ProcessModel> processModel =
          Optional.ofNullable(processModelRepository.findOne(msg.getPmId()));
      if (!processModel.isPresent()) {
        throw new IllegalStateException("Could not find process model");
      }
      processBuilder.processModel(processModel.get());

      msg.getUserGroupAssignments().stream().map(entry -> createSubject(processBuilder, entry))
          .forEach(subject -> subjectRepository.save((SubjectImpl) subject));

      final ProcessInstance processInstance =
          processInstanceRepository.save((ProcessInstanceImpl) processBuilder.build());
      LOG.info("Created new process instance: {}", processInstance);

      final String processInstanceId = "Process-" + processInstance.getPiId();
      final Optional<ActorRef> actorOpt = akkaSelector.findActor(getContext(), processInstanceId);
      if (!actorOpt.isPresent()) {
        getContext().actorOf(springExtension.props("ProcessActor"), processInstanceId);
        LOG.info("Created new actor for process instance: {}", processInstanceId);
      }

      getSender().tell(new ProcessStartMessage.Response(processInstance.getPiId()), getSelf());
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
    } else {
      builder.groupId(entry.getGroupId());
    }

    final Subject subject = builder.build();
    processBuilder.addSubject(subject);
    return subject;
  }

}
