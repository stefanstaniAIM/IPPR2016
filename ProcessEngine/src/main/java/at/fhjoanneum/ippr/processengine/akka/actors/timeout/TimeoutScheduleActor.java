package at.fhjoanneum.ippr.processengine.akka.actors.timeout;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import akka.actor.ActorSystem;
import akka.actor.Cancellable;
import akka.actor.UntypedActor;
import at.fhjoanneum.ippr.persistence.objects.engine.state.SubjectState;
import at.fhjoanneum.ippr.processengine.akka.messages.process.timeout.TimeoutExecuteMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.timeout.TimeoutScheduleCancelMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.timeout.TimeoutScheduleStartMessage;
import at.fhjoanneum.ippr.processengine.repositories.SubjectStateRepository;
import scala.concurrent.duration.Duration;

@Component("TimeoutScheduleActor")
@Scope("prototype")
@Transactional(isolation = Isolation.READ_COMMITTED)
public class TimeoutScheduleActor extends UntypedActor {

  private final static Logger LOG = LoggerFactory.getLogger(TimeoutScheduleActor.class);

  @Autowired
  private ActorSystem actorSystem;

  @Autowired
  private SubjectStateRepository subjectStateRepository;

  private Cancellable scheduler;

  @Override
  public void onReceive(final Object obj) throws Throwable {
    if (obj instanceof TimeoutScheduleStartMessage) {
      handleTimeoutScheduleStartMessage(obj);
    } else if (obj instanceof TimeoutScheduleCancelMessage) {
      handleTimeoutScheduleCancelMessage(obj);
    } else {
      LOG.warn("Unhandled message: [{}]", obj);
      unhandled(obj);
    }
  }

  private void handleTimeoutScheduleStartMessage(final Object obj) {
    final TimeoutScheduleStartMessage msg = (TimeoutScheduleStartMessage) obj;
    final SubjectState subjectState = subjectStateRepository.findOne(msg.getSsId());
    final long timeout =
        subjectState.getCurrentState().getTimeoutTransition().get().getTimeout().longValue();

    final LocalDateTime now = LocalDateTime.now();
    final LocalDateTime lastChanged = subjectState.getLastChanged();
    final long alreadyPassed = ChronoUnit.MINUTES.between(lastChanged, now);

    long actualTimeout = timeout - alreadyPassed;
    if (actualTimeout < 0) {
      actualTimeout = 0;
    }

    LOG.info("Start [{}] min. timeout for [{}]", actualTimeout, subjectState);
    scheduler = actorSystem.scheduler()
        .scheduleOnce(Duration.create(actualTimeout, TimeUnit.MINUTES), () -> {
          getContext().parent().tell(new TimeoutExecuteMessage(subjectState.getSsId()), getSelf());
          getContext().stop(getSelf());
        }, actorSystem.dispatcher());
  }

  private void handleTimeoutScheduleCancelMessage(final Object obj) {
    if (scheduler != null) {
      scheduler.cancel();
      LOG.info("Timeout scheduler is stopped for [{}]",
          ((TimeoutScheduleCancelMessage) obj).getSsId());
      getContext().stop(getSelf());
    } else {
      throw new IllegalStateException("The scheduler is null");
    }
  }
}
