package at.fhjoanneum.ippr.processengine.akka.actors.process;

import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import akka.actor.UntypedActor;
import at.fhjoanneum.ippr.persistence.objects.model.process.ProcessModel;
import at.fhjoanneum.ippr.processengine.akka.messages.process.check.ProcessCheckMessage;
import at.fhjoanneum.ippr.processengine.repositories.ProcessModelRepository;

@Transactional
@Component("ProcessCheckActor")
@Scope("prototype")
public class ProcessCheckActor extends UntypedActor {

  private final static Logger LOG = LoggerFactory.getLogger(ProcessCheckActor.class);

  @Autowired
  private ProcessModelRepository processModelRepository;

  @Override
  public void onReceive(final Object obj) throws Throwable {
    if (obj instanceof ProcessCheckMessage.Request) {
      final ProcessCheckMessage.Request msg = (ProcessCheckMessage.Request) obj;
      LOG.debug("Received ProcessToCheckMessage for PM_ID [{}]", msg.getPmId());

      boolean checkResult = false;

      final Optional<ProcessModel> optProcessModel =
          Optional.ofNullable(processModelRepository.findOne(msg.getPmId()));
      if (optProcessModel.isPresent()) {
        checkResult = true;
      } else {
        LOG.error("Could not find process model for PM_ID [{}]", msg.getPmId());
      }

      LOG.info("Result of process check = {}", checkResult);
      getSender().tell(new ProcessCheckMessage.Response(checkResult), getSelf());
      getContext().stop(getSelf());
    } else {
      unhandled(obj);
    }
  }

}
