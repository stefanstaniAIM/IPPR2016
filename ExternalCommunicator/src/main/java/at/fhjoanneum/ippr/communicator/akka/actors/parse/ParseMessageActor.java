package at.fhjoanneum.ippr.communicator.akka.actors.parse;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;
import at.fhjoanneum.ippr.commons.dto.communicator.BusinessObject;
import at.fhjoanneum.ippr.commons.dto.communicator.BusinessObjectField;
import at.fhjoanneum.ippr.commons.dto.communicator.ExternalCommunicatorMessage;
import at.fhjoanneum.ippr.communicator.akka.config.SpringExtension;
import at.fhjoanneum.ippr.communicator.akka.messages.commands.ConfigRetrievalCommand;
import at.fhjoanneum.ippr.communicator.akka.messages.commands.UpdateMessageStateCommand;
import at.fhjoanneum.ippr.communicator.akka.messages.parse.commands.NotifyProcessEngineCommand;
import at.fhjoanneum.ippr.communicator.akka.messages.parse.commands.ParseMessageCommand;
import at.fhjoanneum.ippr.communicator.akka.messages.parse.commands.ParseMessageCreateCommand;
import at.fhjoanneum.ippr.communicator.akka.messages.parse.commands.StoreInternalDataCommand;
import at.fhjoanneum.ippr.communicator.akka.messages.parse.events.ConfigRetrievedEvent;
import at.fhjoanneum.ippr.communicator.akka.messages.parse.events.NotifyConfigRetrievedEvent;
import at.fhjoanneum.ippr.communicator.feign.ProcessEngineClient;
import at.fhjoanneum.ippr.communicator.parser.ParseResult;
import at.fhjoanneum.ippr.communicator.parser.Parser;
import at.fhjoanneum.ippr.communicator.persistence.objects.messageflow.MessageState;

@Transactional(isolation = Isolation.READ_COMMITTED)
@Component("ParseMessageActor")
@Scope("prototype")
public class ParseMessageActor extends AbstractActor {

  private final static Logger LOG = LoggerFactory.getLogger(ParseMessageActor.class);

  @Autowired
  private ProcessEngineClient processEngineClient;

  @Autowired
  private SpringExtension springExtension;

  public ParseMessageActor() {
    receive(ReceiveBuilder
        .match(ParseMessageCreateCommand.class,
            cmd -> getDBPersistenceActor().tell(cmd, getContext().parent()))
        .match(ParseMessageCommand.class, this::handleParseMessageCommand)
        .match(ConfigRetrievedEvent.class, this::handleConfigRetrievedEvent)
        .match(NotifyProcessEngineCommand.class, this::handleNotifyProcessEngineCommand)
        .match(NotifyConfigRetrievedEvent.class, this::handleNotifyConfigRetrievedEvent)
        .matchAny(o -> LOG.warn("Unhandled message [{}]", o)).build());
  }

  private void handleParseMessageCommand(final ParseMessageCommand cmd) {
    getDBPersistenceActor().tell(new ConfigRetrievalCommand(cmd.getId()), self());
  }

  private void handleConfigRetrievedEvent(final ConfigRetrievedEvent evt) throws Exception {
    final Parser parser =
        getClass().getClassLoader().loadClass(evt.getBasicConfiguration().getParserClass())
            .asSubclass(Parser.class).newInstance();

    final ParseResult result =
        parser.parse(evt.getData(), evt.getBasicConfiguration().getMessageProtocol(),
            evt.getBasicConfiguration().getDataTypeParser(),
            evt.getBasicConfiguration().getConfiguration());

    getDBPersistenceActor().tell(
        new StoreInternalDataCommand(evt.getId(), evt.getBasicConfiguration().getId(), result),
        getContext().parent());
  }

  private void handleNotifyProcessEngineCommand(final NotifyProcessEngineCommand cmd) {
    getDBPersistenceActor().tell(cmd, self());
  }

  private void handleNotifyConfigRetrievedEvent(final NotifyConfigRetrievedEvent evt) {
    final Set<BusinessObject> objects = new HashSet<>();
    evt.getData().getObjects().values().stream().forEachOrdered(obj -> {
      final Set<BusinessObjectField> fields =
          obj.getFields().values().stream().map(field -> new BusinessObjectField(field.getName(),
              field.getDataType().name(), field.getValue())).collect(Collectors.toSet());
      objects.add(new BusinessObject(obj.getName(), fields));
    });

    processEngineClient.notify(new ExternalCommunicatorMessage(evt.getTransferId(), objects));

    getDBPersistenceActor().tell(new UpdateMessageStateCommand(evt.getId(), MessageState.RECEIVED),
        getContext().parent());
  }

  private ActorRef getDBPersistenceActor() {
    return getContext().actorOf(springExtension.props("ParsePersistenceActor"),
        UUID.randomUUID().toString());
  }
}
