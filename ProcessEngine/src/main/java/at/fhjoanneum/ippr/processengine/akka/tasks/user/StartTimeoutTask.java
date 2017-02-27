package at.fhjoanneum.ippr.processengine.akka.tasks.user;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import akka.actor.ActorRef;
import akka.actor.UntypedActorContext;
import at.fhjoanneum.ippr.persistence.objects.engine.state.SubjectState;
import at.fhjoanneum.ippr.processengine.akka.config.SpringExtension;
import at.fhjoanneum.ippr.processengine.akka.messages.process.timeout.TimeoutScheduleStartMessage;
import at.fhjoanneum.ippr.processengine.akka.tasks.AbstractTask;
import at.fhjoanneum.ippr.processengine.repositories.SubjectStateRepository;

@Component("User.StartTimeoutTask")
@Scope("prototype")
public class StartTimeoutTask extends AbstractTask<TimeoutScheduleStartMessage> {

  private static final String TIMEOUT_SCHEDULER = "TIMEOUT_SCHEDULER_";

  @Autowired
  private SpringExtension springExtension;

  @Autowired
  private SubjectStateRepository subjectStateRepository;

  public StartTimeoutTask(final UntypedActorContext parentContext) {
    super(parentContext);
  }

  @Override
  public boolean canHandle(final Object obj) {
    return obj instanceof TimeoutScheduleStartMessage;
  }

  @Override
  public void execute(final TimeoutScheduleStartMessage message) throws Exception {
    final SubjectState subjectState = subjectStateRepository.findOne(message.getSsId());
    final String id =
        StringUtils.isBlank(message.getTimeoutActorId()) ? getId() : message.getTimeoutActorId();
    subjectState.setTimeoutActor(id);

    final ActorRef timeoutScheduler =
        getParentContext().actorOf(springExtension.props("TimeoutScheduleActor"), id);
    timeoutScheduler.forward(message, getParentContext());
  }

  private static String getId() {
    return TIMEOUT_SCHEDULER + UUID.randomUUID().toString();
  }
}
