package at.fhjoanneum.ippr.processengine.akka.actors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import akka.actor.UntypedActor;
import at.fhjoanneum.ippr.persistence.objects.engine.enums.ProcessInstanceState;
import at.fhjoanneum.ippr.processengine.akka.messages.analysis.AmountOfActiveProcessesMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.analysis.AmountOfProcessesPerUserMessage;
import at.fhjoanneum.ippr.processengine.repositories.ProcessInstanceRepository;

@Component("ProcessAnalysisActor")
@Scope("prototype")
public class ProcessAnalysisActor extends UntypedActor {

  @Autowired
  private ProcessInstanceRepository processInstanceRepository;

  @Override
  public void onReceive(final Object obj) throws Throwable {
    if (obj instanceof AmountOfActiveProcessesMessage.Request) {

      getSender().tell(
          new AmountOfActiveProcessesMessage.Response(
              processInstanceRepository.getAmountOfProcesses(ProcessInstanceState.ACTIVE.name())),
          getSelf());
      getContext().stop(getSelf());
    } else if (obj instanceof AmountOfProcessesPerUserMessage.Request) {
      final AmountOfProcessesPerUserMessage.Request msg =
          (AmountOfProcessesPerUserMessage.Request) obj;

      getSender().tell(new AmountOfProcessesPerUserMessage.Response(processInstanceRepository
          .getAmountOfProcessesPerUser(msg.getState().name(), msg.getUserId())), getSelf());
      getContext().stop(getSelf());
    } else {
      unhandled(obj);
    }
  }
}
