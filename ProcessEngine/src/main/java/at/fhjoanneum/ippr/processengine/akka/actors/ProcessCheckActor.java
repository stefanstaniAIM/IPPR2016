package at.fhjoanneum.ippr.processengine.akka.actors;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import akka.actor.UntypedActor;
import at.fhjoanneum.ippr.persistence.objects.model.process.ProcessModel;
import at.fhjoanneum.ippr.persistence.objects.model.subject.SubjectModel;
import at.fhjoanneum.ippr.processengine.akka.messages.process.check.ProcessCheckResponseMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.check.ProcessToCheckMessage;
import at.fhjoanneum.ippr.processengine.repositories.ProcessModelRepository;

@Component("ProcessCheckActor")
@Scope("prototype")
public class ProcessCheckActor extends UntypedActor {

  private final static Logger LOG = LoggerFactory.getLogger(ProcessCheckActor.class);

  @Autowired
  private ProcessModelRepository processModelRepository;

  @Transactional
  @Override
  public void onReceive(final Object obj) throws Throwable {
    if (obj instanceof ProcessToCheckMessage) {
      final ProcessToCheckMessage msg = (ProcessToCheckMessage) obj;
      LOG.debug("Received ProcessToCheckMessage for PM_ID [{}]", msg.getPmId());

      boolean checkResult = false;

      final Optional<ProcessModel> optProcessModel =
          Optional.ofNullable(processModelRepository.findOne(msg.getPmId()));
      if (optProcessModel.isPresent()) {
        final ProcessModel pm = optProcessModel.get();

        final List<Long> storedSmIds =
            pm.getSubjectModels().stream().map(SubjectModel::getSmId).collect(Collectors.toList());

        checkResult = true;

      } else {
        LOG.error("Could not find process model for PM_ID [{}]", msg.getPmId());
      }

      LOG.info("Result of process check = {}", checkResult);
      getSender().tell(new ProcessCheckResponseMessage(checkResult), getSelf());
    } else {
      unhandled(obj);
    }
  }

}
