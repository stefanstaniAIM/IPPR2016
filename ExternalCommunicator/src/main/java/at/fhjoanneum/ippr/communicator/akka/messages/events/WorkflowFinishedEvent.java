package at.fhjoanneum.ippr.communicator.akka.messages.events;

import at.fhjoanneum.ippr.communicator.akka.messages.AbstractEvent;

public class WorkflowFinishedEvent extends AbstractEvent {

  public WorkflowFinishedEvent(final String actorId) {
    super(actorId);
  }
}
