package at.fhjoanneum.ippr.processengine.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import akka.actor.UntypedActor;
import at.fhjoanneum.ippr.processengine.akka.messages.process.ProcessStartMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.ProcessStartedMessage;
import at.fhjoanneum.ippr.processengine.repositories.ProcessInstanceRepository;
import at.fhjoanneum.ippr.processengine.repositories.ProcessModelRepository;

@Component("ProcessActor")
@Scope("prototype")
public class ProcessActor extends UntypedActor {

  private final static Logger LOG = LoggerFactory.getLogger(ProcessActor.class);

  @Autowired
  private ProcessModelRepository processModelRepository;

  @Autowired
  private ProcessInstanceRepository processInstanceRepository;

  @Override
  public void onReceive(final Object msg) throws Throwable {
    if (msg instanceof ProcessStartMessage) {
      handleProcessStartMessage((ProcessStartMessage) msg);
    } else {
      unhandled(msg);
    }
  }

  private void handleProcessStartMessage(final ProcessStartMessage msg) {
    LOG.info("Handle ProcessStartMessage and will create new process instance");

    getSender().tell(new ProcessStartedMessage(2222L), getSelf());
    getSender().tell(new akka.actor.Status.Failure(new Exception("oh no no no")), getSelf());
  }
}
