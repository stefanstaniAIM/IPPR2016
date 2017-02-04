package at.fhjoanneum.ippr.processengine.akka.actors.analysis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import akka.actor.UntypedActor;
import at.fhjoanneum.ippr.processengine.akka.messages.analysis.FinishedProcessesInRangeForUserMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.analysis.FinishedProcessesInRangeMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.analysis.FinishedProcessesInRangeMessage.Request;
import at.fhjoanneum.ippr.processengine.akka.messages.analysis.ProcessesInStateMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.analysis.ProcessesInStateMessage.Response;
import at.fhjoanneum.ippr.processengine.akka.messages.analysis.ProcessesInStatePerUserMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.analysis.StartedProcessesInRangeForUserMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.analysis.StartedProcessesInRangeMessage;
import at.fhjoanneum.ippr.processengine.repositories.ProcessInstanceRepository;

@Component("ProcessAnalysisActor")
@Scope("prototype")
public class ProcessAnalysisActor extends UntypedActor {

  @Autowired
  private ProcessInstanceRepository processInstanceRepository;

  @Override
  public void onReceive(final Object obj) throws Throwable {
    try {
      if (obj instanceof ProcessesInStateMessage.Request) {
        handleProcessesInStateMessage(obj);
      } else if (obj instanceof ProcessesInStatePerUserMessage.Request) {
        handleProcessesInStatePerUserMessage(obj);
      } else if (obj instanceof StartedProcessesInRangeMessage.Request) {
        handleStartedProcessesInRangeMessage(obj);
      } else if (obj instanceof StartedProcessesInRangeForUserMessage.Request) {
        handleStartedProcessesInRangeForUserMessage(obj);
      } else if (obj instanceof FinishedProcessesInRangeMessage.Request) {
        handleFinishedProcessInRangeMessage(obj);
      } else if (obj instanceof FinishedProcessesInRangeForUserMessage.Request) {
        handleFinishedProcessInRangeForUserMessage(obj);
      } else {
        unhandled(obj);
      }
    } finally {
      getContext().stop(getSelf());
    }
  }

  private void handleProcessesInStateMessage(final Object obj) {
    final ProcessesInStateMessage.Request msg = (ProcessesInStateMessage.Request) obj;
    final Response response = new ProcessesInStateMessage.Response(
        processInstanceRepository.getAmountOfProcessesInState(msg.getState()));
    getSender().tell(response, getSelf());
  }

  private void handleProcessesInStatePerUserMessage(final Object obj) {
    final ProcessesInStatePerUserMessage.Request msg = (ProcessesInStatePerUserMessage.Request) obj;

    getSender().tell(new ProcessesInStatePerUserMessage.Response(processInstanceRepository
        .getAmountOfProcessesInStatePerUser(msg.getState(), msg.getUserId())), getSelf());
  }

  private void handleStartedProcessesInRangeMessage(final Object obj) {
    final StartedProcessesInRangeMessage.Request msg = (StartedProcessesInRangeMessage.Request) obj;
    final StartedProcessesInRangeMessage.Response response =
        new StartedProcessesInRangeMessage.Response(processInstanceRepository
            .getAmountOfStartedProcessesBetweenRange(msg.getStart(), msg.getEnd()));
    getSender().tell(response, getSelf());
  }

  private void handleStartedProcessesInRangeForUserMessage(final Object obj) {
    final StartedProcessesInRangeForUserMessage.Request msg =
        (StartedProcessesInRangeForUserMessage.Request) obj;
    final StartedProcessesInRangeForUserMessage.Response response =
        new StartedProcessesInRangeForUserMessage.Response(
            processInstanceRepository.getAmountOfStartedProcessesBetweenRangeForUser(msg.getStart(),
                msg.getEnd(), msg.getUser()));
    getSender().tell(response, getSelf());
  }

  private void handleFinishedProcessInRangeMessage(final Object obj) {
    final Request msg = (FinishedProcessesInRangeMessage.Request) obj;
    final FinishedProcessesInRangeMessage.Response response =
        new FinishedProcessesInRangeMessage.Response(processInstanceRepository
            .getAmountOfFinishedProcessesBetweenRange(msg.getStart(), msg.getEnd()));
    getSender().tell(response, getSelf());
  }

  private void handleFinishedProcessInRangeForUserMessage(final Object obj) {
    final FinishedProcessesInRangeForUserMessage.Request msg =
        (FinishedProcessesInRangeForUserMessage.Request) obj;
    final FinishedProcessesInRangeForUserMessage.Response response =
        new FinishedProcessesInRangeForUserMessage.Response(
            processInstanceRepository.getAmountOfFinishedProcessesBetweenRangeForUser(
                msg.getStart(), msg.getEnd(), msg.getUser()));
    getSender().tell(response, getSelf());
  }
}
