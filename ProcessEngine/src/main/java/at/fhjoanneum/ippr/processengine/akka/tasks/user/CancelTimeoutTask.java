package at.fhjoanneum.ippr.processengine.akka.tasks.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import akka.actor.ActorRef;
import akka.actor.UntypedActorContext;
import at.fhjoanneum.ippr.persistence.objects.engine.state.SubjectState;
import at.fhjoanneum.ippr.processengine.akka.AkkaSelector;
import at.fhjoanneum.ippr.processengine.akka.messages.process.timeout.TimeoutScheduleCancelMessage;
import at.fhjoanneum.ippr.processengine.akka.tasks.AbstractTask;
import at.fhjoanneum.ippr.processengine.repositories.SubjectStateRepository;

@Component("User.CancelTimeoutTask")
@Scope("prototype")
public class CancelTimeoutTask extends AbstractTask<TimeoutScheduleCancelMessage> {

  @Autowired
  private AkkaSelector akkaSelector;

  @Autowired
  private SubjectStateRepository subjectStateRepository;

  public CancelTimeoutTask(final UntypedActorContext parentContext) {
    super(parentContext);
  }

  @Override
  public boolean canHandle(final Object obj) {
    return obj instanceof TimeoutScheduleCancelMessage;
  }

  @Override
  public void execute(final TimeoutScheduleCancelMessage message) throws Exception {
    final SubjectState subjectState = subjectStateRepository.findOne(message.getSsId());
    final String actorId = subjectState.getTimeoutActor();
    subjectState.setTimeoutActor(null);

    final Optional<ActorRef> timeoutScheduler = akkaSelector.findActor(getParentContext(), actorId);
    if (timeoutScheduler.isPresent()) {
      timeoutScheduler.get().forward(message, getParentContext());
    }
  }
}
