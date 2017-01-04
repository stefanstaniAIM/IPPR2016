package at.fhjoanneum.ippr.processengine.akka.tasks.process;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import at.fhjoanneum.ippr.commons.dto.processengine.ProcessInfoDTO;
import at.fhjoanneum.ippr.persistence.objects.engine.enums.ProcessInstanceState;
import at.fhjoanneum.ippr.persistence.objects.engine.process.ProcessInstance;
import at.fhjoanneum.ippr.processengine.akka.messages.process.stop.ProcessStopMessage;
import at.fhjoanneum.ippr.processengine.akka.tasks.AbstractTask;
import at.fhjoanneum.ippr.processengine.akka.tasks.TaskCallback;
import at.fhjoanneum.ippr.processengine.repositories.ProcessInstanceRepository;

@Component("Process.ProcessStopTask")
@Scope("prototype")
public class ProcessStopTask extends AbstractTask<ProcessStopMessage.Request> {

  public <T> ProcessStopTask(final TaskCallback<T> callback) {
    super(callback);
  }

  private final static Logger LOG = LoggerFactory.getLogger(ProcessStopTask.class);

  @Autowired
  private ProcessInstanceRepository processInstanceRepository;

  @Override
  public boolean canHandle(final Object obj) {
    return obj instanceof ProcessStopMessage.Request;
  }

  @Override
  public void execute(final ProcessStopMessage.Request msg) throws Exception {
    final Optional<ProcessInstance> processOpt =
        Optional.ofNullable(processInstanceRepository.findOne(msg.getPiId()));
    if (!processOpt.isPresent()) {
      throw new IllegalArgumentException();
    }

    final ProcessInstance process = processOpt.get();
    if (!process.getState().equals(ProcessInstanceState.ACTIVE)) {
      throw new IllegalStateException();
    }

    process.setState(ProcessInstanceState.CANCELLED_BY_USER);

    TransactionSynchronizationManager
        .registerSynchronization(new TransactionSynchronizationAdapter() {
          @Override
          public void afterCommit() {
            LOG.info("Process was stopped: {}", process);
            final ProcessInfoDTO dto = new ProcessInfoDTO(process.getPiId(), process.getStartTime(),
                process.getEndTime(), process.getProcessModel().getName(), process.getStartUserId(),
                process.getState().name());
            getSender().tell(new ProcessStopMessage.Response(dto), getSelf());
            callback();
          }
        });
  }

}
