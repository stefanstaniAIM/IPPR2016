package at.fhjoanneum.ippr.processengine.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.UntypedActor;
import at.fhjoanneum.ippr.processengine.akka.messages.process.ProcessStartedMessage;

public class ProcessActor extends UntypedActor {

  private final static Logger LOG = LoggerFactory.getLogger(ProcessActor.class);

  @Override
  public void onReceive(final Object arg0) throws Throwable {
    LOG.info("Reply with ProcessStartedMessage");
    getSender().tell(new ProcessStartedMessage(2222L), getSelf());
    // getSender().tell(new akka.actor.Status.Failure(new Exception()), getSelf());
  }

}
