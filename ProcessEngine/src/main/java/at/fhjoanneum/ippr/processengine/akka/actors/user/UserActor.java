package at.fhjoanneum.ippr.processengine.akka.actors.user;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import at.fhjoanneum.ippr.commons.dto.processengine.TaskDTO;
import at.fhjoanneum.ippr.persistence.entities.engine.enums.ReceiveSubjectState;
import at.fhjoanneum.ippr.persistence.entities.engine.state.SubjectStateBuilder;
import at.fhjoanneum.ippr.persistence.entities.engine.state.SubjectStateImpl;
import at.fhjoanneum.ippr.persistence.objects.engine.enums.ProcessInstanceState;
import at.fhjoanneum.ippr.persistence.objects.engine.process.ProcessInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.state.SubjectState;
import at.fhjoanneum.ippr.persistence.objects.engine.subject.Subject;
import at.fhjoanneum.ippr.persistence.objects.model.enums.StateFunctionType;
import at.fhjoanneum.ippr.persistence.objects.model.state.State;
import at.fhjoanneum.ippr.processengine.akka.config.Global;
import at.fhjoanneum.ippr.processengine.akka.messages.EmptyMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.info.TasksOfUserMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.initialize.UserActorInitializeMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.stop.ProcessStopMessage;
import at.fhjoanneum.ippr.processengine.akka.messages.process.wakeup.UserActorWakeUpMessage;
import at.fhjoanneum.ippr.processengine.repositories.CustomTypesQueriesRepository;
import at.fhjoanneum.ippr.processengine.repositories.ProcessInstanceRepository;
import at.fhjoanneum.ippr.processengine.repositories.StateRepository;
import at.fhjoanneum.ippr.processengine.repositories.SubjectRepository;
import at.fhjoanneum.ippr.processengine.repositories.SubjectStateRepository;

@Transactional
@Component("UserActor")
@Scope("prototype")
public class UserActor extends UntypedActor {

  private final static Logger LOG = LoggerFactory.getLogger(UserActor.class);

  @Autowired
  private ProcessInstanceRepository processInstanceRepository;
  @Autowired
  private SubjectRepository subjectRepository;
  @Autowired
  private StateRepository stateRepository;
  @Autowired
  private SubjectStateRepository subjectStateRepository;
  @Autowired
  private CustomTypesQueriesRepository customTypesQueriesRepository;

  private final Long userId;

  public UserActor(final Long userId) {
    this.userId = userId;
  }

  @Override
  public void onReceive(final Object obj) throws Throwable {
    if (obj instanceof UserActorInitializeMessage.Request) {
      handleActorInitializeMessage(obj);
    } else if (obj instanceof UserActorWakeUpMessage.Request) {
      handleUserWakeUpMessage(obj);
    } else if (obj instanceof ProcessStopMessage.Request) {
      handleProcessStopMessage(obj);
    } else if (obj instanceof TasksOfUserMessage.Request) {
      handleTasksOfUserMessage(obj);
    } else {
      unhandled(obj);
    }
  }

  private void handleActorInitializeMessage(final Object obj) {
    final UserActorInitializeMessage.Request msg = (UserActorInitializeMessage.Request) obj;

    final ProcessInstance processInstance =
        Optional.ofNullable(processInstanceRepository.findOne(msg.getPiId())).get();

    final Subject subject = Optional.ofNullable(subjectRepository.findOne(msg.getSId())).get();

    final State state = Optional
        .ofNullable(stateRepository.getStartStateOfSubject(subject.getSubjectModel().getSmId()))
        .get();

    ReceiveSubjectState receiveSubjectState = null;
    if (state.getFunctionType().equals(StateFunctionType.RECEIVE)) {
      receiveSubjectState = ReceiveSubjectState.TO_RECEIVE;
    }

    final SubjectState subjectState = new SubjectStateBuilder().processInstance(processInstance)
        .subject(subject).state(state).receiveSubjectState(receiveSubjectState).build();

    subjectStateRepository.save((SubjectStateImpl) subjectState);
    LOG.info("Subject is now in initial state: {}", subjectState);

    final ActorRef sender = getSender();

    TransactionSynchronizationManager
        .registerSynchronization(new TransactionSynchronizationAdapter() {
          @Override
          public void afterCommit() {
            // notify user supervisor actor
            sender.tell(new EmptyMessage(), getSelf());
          }
        });

    if (userId.longValue() == Global.DESTROY_ID.longValue()) {
      getContext().stop(getSelf());
    }
  }

  private void handleUserWakeUpMessage(final Object obj) {
    final UserActorWakeUpMessage.Request msg = (UserActorWakeUpMessage.Request) obj;
    getSender().tell(new UserActorWakeUpMessage.Request(msg.getUserId()), getSelf());
  }

  private void handleProcessStopMessage(final Object obj) {
    final Long amountOfActiveProcesses = processInstanceRepository
        .getAmountOfProcessesPerUser(ProcessInstanceState.ACTIVE.name(), userId);

    if (amountOfActiveProcesses.longValue() == 0) {
      getContext().stop(getSelf());
      LOG.info("No active processes anymore for user [{}], therefore shutdown actor",
          getSelf().path().name());
    }
  }

  private void handleTasksOfUserMessage(final Object obj) {
    final TasksOfUserMessage.Request msg = (TasksOfUserMessage.Request) obj;

    final List<TaskDTO> tasks = customTypesQueriesRepository.getTasksOfUser(msg.getUserId());
    getSender().tell(new TasksOfUserMessage.Response(tasks), getSelf());
  }
}
