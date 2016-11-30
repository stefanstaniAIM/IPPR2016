package at.fhjoanneum.ippr.processengine.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import akka.actor.UntypedActor;
import at.fhjoanneum.ippr.processengine.repositories.ProcessInstanceRepository;
import at.fhjoanneum.ippr.processengine.repositories.ProcessModelRepository;
import at.fhjoanneum.ippr.processengine.repositories.SubjectModelRepository;
import at.fhjoanneum.ippr.processengine.repositories.SubjectRepository;

@Component("ProcessActor")
@Scope("prototype")
public class ProcessActor extends UntypedActor {

  private final static Logger LOG = LoggerFactory.getLogger(ProcessActor.class);

  @Autowired
  private ProcessModelRepository processModelRepository;

  @Autowired
  private SubjectModelRepository subjectModelRepository;

  @Autowired
  private ProcessInstanceRepository processInstanceRepository;

  @Autowired
  private SubjectRepository subjectRepository;

  @Override
  public void onReceive(final Object msg) throws Throwable {
    unhandled(msg);
  }
}
