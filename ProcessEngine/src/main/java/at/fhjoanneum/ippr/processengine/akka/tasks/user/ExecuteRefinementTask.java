package at.fhjoanneum.ippr.processengine.akka.tasks.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import at.fhjoanneum.ippr.processengine.akka.messages.process.refinement.ExecuteRefinementMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.refinement.ExecuteRefinementMessage.Request;
import at.fhjoanneum.ippr.processengine.akka.tasks.AbstractTask;

@Component("User.ExecuteRefinement")
@Scope("prototype")
public class ExecuteRefinementTask extends AbstractTask<ExecuteRefinementMessage.Request> {

  private final static Logger LOG = LoggerFactory.getLogger(ExecuteRefinementTask.class);

  @Override
  public boolean canHandle(final Object obj) {
    return obj instanceof ExecuteRefinementMessage.Request;
  }

  @Override
  public void execute(final Request message) throws Exception {
    LOG.debug("yes i am so there");

    // notify user -> refinement
    // get state object
    // execute task
    // change state object
    // am besten in eigenem task
  }

}
