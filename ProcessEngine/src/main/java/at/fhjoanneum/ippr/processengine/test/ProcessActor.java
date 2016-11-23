package at.fhjoanneum.ippr.processengine.test;

import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import akka.actor.UntypedActor;
import at.fhjoanneum.ippr.persistence.entities.engine.process.ProcessInstanceBuilder;
import at.fhjoanneum.ippr.persistence.entities.engine.process.ProcessInstanceImpl;
import at.fhjoanneum.ippr.persistence.entities.engine.subject.SubjectBuilder;
import at.fhjoanneum.ippr.persistence.entities.engine.subject.SubjectImpl;
import at.fhjoanneum.ippr.persistence.objects.engine.process.ProcessInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.subject.Subject;
import at.fhjoanneum.ippr.persistence.objects.model.process.ProcessModel;
import at.fhjoanneum.ippr.persistence.objects.model.subject.SubjectModel;
import at.fhjoanneum.ippr.processengine.akka.messages.process.ProcessStartMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.ProcessStartMessage.UserGroupAssignment;
import at.fhjoanneum.ippr.processengine.akka.messages.process.ProcessStartedMessage;
import at.fhjoanneum.ippr.processengine.repositories.ProcessInstanceRepository;
import at.fhjoanneum.ippr.processengine.repositories.ProcessModelRepository;
import at.fhjoanneum.ippr.processengine.repositories.SubjectModelRepository;
import at.fhjoanneum.ippr.processengine.repositories.SubjectReposistory;

@Component("ProcessActor")
@Scope("prototype")
public class ProcessActor extends UntypedActor {

  private final static Logger LOG = LoggerFactory.getLogger(ProcessActor.class);

  @Autowired
  private ProcessModelRepository processModelRepository;

  @Autowired
  private SubjectModelRepository subjectModelRepository;

  @Autowired
  private ProcessInstanceRepository processInstanceRepository;

  @Autowired
  private SubjectReposistory subjectRepository;

  @Override
  public void onReceive(final Object msg) throws Throwable {
    if (msg instanceof ProcessStartMessage) {
      handleProcessStartMessage((ProcessStartMessage) msg);
    } else {
      unhandled(msg);
    }
  }

  @Transactional
  private void handleProcessStartMessage(final ProcessStartMessage msg) {
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

      getSender().tell(new ProcessStartedMessage(processInstance.getPiId()), getSelf());
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
