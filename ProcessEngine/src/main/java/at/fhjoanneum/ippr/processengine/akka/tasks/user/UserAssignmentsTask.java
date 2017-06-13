package at.fhjoanneum.ippr.processengine.akka.tasks.user;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import akka.actor.ActorRef;
import at.fhjoanneum.ippr.persistence.entities.engine.subject.SubjectImpl;
import at.fhjoanneum.ippr.persistence.objects.engine.subject.Subject;
import at.fhjoanneum.ippr.processengine.akka.messages.process.workflow.AssignUsersMessage;
import at.fhjoanneum.ippr.processengine.akka.tasks.AbstractTask;
import at.fhjoanneum.ippr.processengine.repositories.SubjectRepository;

@Component("UserSupervisor.AssignUsersTask")
@Scope("prototype")
public class UserAssignmentsTask extends AbstractTask<AssignUsersMessage.Request> {

  private final static Logger LOG = LoggerFactory.getLogger(UserAssignmentsTask.class);

  @Autowired
  private SubjectRepository subjectRepository;

  @Override
  public boolean canHandle(final Object obj) {
    return obj instanceof AssignUsersMessage.Request;
  }

  @Override
  public void execute(final AssignUsersMessage.Request request) throws Exception {
    request.getUserAssignments().forEach(assignment -> {
      final Subject subject = subjectRepository
          .getSubjectForSubjectModelInProcess(request.getPiId(), assignment.getSmId());
      if (subject != null) {
        subject.setUser(assignment.getUserId());
        subjectRepository.save((SubjectImpl) subject);
        LOG.info("New user for subject: {}", subject);
      }
    });

    final ActorRef sender = getSender();
    TransactionSynchronizationManager
        .registerSynchronization(new TransactionSynchronizationAdapter() {
          @Override
          public void afterCommit() {
            sender.tell(new AssignUsersMessage.Response(), getSelf());
          }
        });

  }
}
