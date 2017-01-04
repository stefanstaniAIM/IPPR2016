package at.fhjoanneum.ippr.processengine.akka.tasks.process;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import at.fhjoanneum.ippr.commons.dto.processengine.ProcessInfoDTO;
import at.fhjoanneum.ippr.persistence.objects.engine.enums.ProcessInstanceState;
import at.fhjoanneum.ippr.persistence.objects.engine.process.ProcessInstance;
import at.fhjoanneum.ippr.processengine.akka.messages.process.info.ProcessInfoMessage;
import at.fhjoanneum.ippr.processengine.akka.tasks.AbstractTask;
import at.fhjoanneum.ippr.processengine.repositories.ProcessInstanceRepository;

@Component("ProcessSupervisor.ProcessInfoTask")
@Scope("prototype")
public class ProcessInfoTask extends AbstractTask<ProcessInfoMessage.Request> {

  private final static Logger LOG = LoggerFactory.getLogger(ProcessInfoTask.class);

  @Autowired
  private ProcessInstanceRepository processInstanceRepository;

  @Override
  public boolean canHandle(final Object obj) {
    return obj instanceof ProcessInfoMessage.Request;
  }

  @Override
  public void execute(final ProcessInfoMessage.Request msg) throws Exception {
    final PageRequest pageRequest =
        new PageRequest(msg.getPage(), msg.getSize(), new Sort(Sort.Direction.DESC, "startTime"));


    List<ProcessInstance> content = null;
    if (msg.getUser() == null) {
      LOG.debug("Received ProcessInfoMessage to show all processes");
      content = Lists.newArrayList(processInstanceRepository
          .getProcessesInfoOfState(pageRequest, ProcessInstanceState.valueOf(msg.getState()))
          .getContent());
    } else {
      LOG.debug("Received ProcessInfoMessage to show all processes of involved user [{}]",
          msg.getUser());
      content =
          Lists.newArrayList(processInstanceRepository.getProcessesInfoOfUserAndState(pageRequest,
              msg.getUser(), ProcessInstanceState.valueOf(msg.getState())).getContent());
    }

    final List<ProcessInfoDTO> processesInfo = content.stream().map(process -> {
      final String processName = process.getProcessModel().getName();
      return new ProcessInfoDTO(process.getPiId(), process.getStartTime(), process.getEndTime(),
          processName, process.getStartUserId());
    }).collect(Collectors.toList());

    getSender().tell(new ProcessInfoMessage.Response(processesInfo), getSelf());
  }
}
