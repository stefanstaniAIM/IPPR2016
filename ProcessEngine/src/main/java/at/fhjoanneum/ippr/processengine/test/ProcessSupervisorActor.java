package at.fhjoanneum.ippr.processengine.test;

import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import at.fhjoanneum.ippr.processengine.akka.AkkaSelector;
import at.fhjoanneum.ippr.processengine.akka.config.SpringExtension;
import at.fhjoanneum.ippr.processengine.akka.messages.process.ProcessStartMessage;

@Component("ProcessSupervisorActor")
@Scope("prototype")
public class ProcessSupervisorActor extends UntypedActor {

  private final static Logger LOG = LoggerFactory.getLogger(ProcessSupervisorActor.class);

  private final AkkaSelector akkaSelector = new AkkaSelector();

  @Autowired
  private SpringExtension springExtension;

  @Override
  public void onReceive(final Object msg) throws Throwable {
    if (msg instanceof ProcessStartMessage) {
      LOG.info("Received {} and forward it to process", msg);

      // TODO retrieve everything from database and create new instance
      final int id = new Random().nextInt();
      final String processInstanceId = "Process_" + id;

      final Optional<ActorRef> actorOpt = akkaSelector.findActor(getContext(), processInstanceId);
      if (!actorOpt.isPresent()) {
        getContext().actorOf(springExtension.props("ProcessActor"), processInstanceId).forward(msg,
            getContext());

        // getContext().actorOf(Props.create(ProcessActor.class), processInstanceId).forward(msg,
        // getContext());
      } else {
        actorOpt.get().forward(msg, getContext());
      }
    } else {
      unhandled(msg);
    }
  }

}
