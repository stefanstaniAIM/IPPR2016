package at.fhjoanneum.ippr.processengine.akka.actors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import akka.actor.UntypedActor;
import at.fhjoanneum.ippr.processengine.akka.messages.analysis.AmountOfActiveProcessesMessages;
import at.fhjoanneum.ippr.processengine.repositories.ProcessInstanceRepository;

@Component("ProcessAnalysisActor")
@Scope("prototype")
public class ProcessAnalysisActor extends UntypedActor {

  @Autowired
  private ProcessInstanceRepository processInstanceRepository;

  @Override
  public void onReceive(final Object obj) throws Throwable {
    if (obj instanceof AmountOfActiveProcessesMessages.Request) {

      getSender().tell(new AmountOfActiveProcessesMessages.Response(
          processInstanceRepository.getAmountOfActiveProcesses()), getSelf());
      getContext().stop(getSelf());
    } else {
      unhandled(obj);
    }
  }
}
