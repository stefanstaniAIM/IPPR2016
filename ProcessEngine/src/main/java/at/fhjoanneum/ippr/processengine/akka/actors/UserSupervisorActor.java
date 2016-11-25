package at.fhjoanneum.ippr.processengine.akka.actors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import akka.actor.UntypedActor;
import at.fhjoanneum.ippr.processengine.akka.AkkaSelector;

@Component("UserSupervisorActor")
@Scope("prototype")
public class UserSupervisorActor extends UntypedActor {

  private final static Logger LOG = LoggerFactory.getLogger(UserSupervisorActor.class);

  private final AkkaSelector akkaSelector = new AkkaSelector();

  @Override
  public void onReceive(final Object obj) throws Throwable {
    unhandled(obj);
  }

}
