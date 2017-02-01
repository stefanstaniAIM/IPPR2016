package at.fhjoanneum.ippr.processengine.akka.actors.analysis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import akka.actor.UntypedActor;
import at.fhjoanneum.ippr.processengine.akka.messages.analysis.AmountOfProcessesInStateMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.analysis.AmountOfProcessesInStateMessage.Response;
import at.fhjoanneum.ippr.processengine.akka.messages.analysis.AmountOfProcessesInStatePerUserMessage;
import at.fhjoanneum.ippr.processengine.repositories.ProcessInstanceRepository;

@Component("ProcessAnalysisActor")
@Scope("prototype")
public class ProcessAnalysisActor extends UntypedActor {

  @Autowired
  private ProcessInstanceRepository processInstanceRepository;

  @Override
  public void onReceive(final Object obj) throws Throwable {
    if (obj instanceof AmountOfProcessesInStateMessage.Request) {

      final AmountOfProcessesInStateMessage.Request msg =
          (AmountOfProcessesInStateMessage.Request) obj;
      final Response response = new AmountOfProcessesInStateMessage.Response(
          processInstanceRepository.getAmountOfProcessesInState(msg.getState()));
      getSender().tell(response, getSelf());
      getContext().stop(getSelf());
    } else if (obj instanceof AmountOfProcessesInStatePerUserMessage.Request) {
      final AmountOfProcessesInStatePerUserMessage.Request msg =
          (AmountOfProcessesInStatePerUserMessage.Request) obj;

      getSender().tell(new AmountOfProcessesInStatePerUserMessage.Response(processInstanceRepository
          .getAmountOfProcessesInStatePerUser(msg.getState(), msg.getUserId())), getSelf());
      getContext().stop(getSelf());
    } else {
      unhandled(obj);
    }
  }
}
