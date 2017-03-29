package at.fhjoanneum.ippr.communicator.akka.messages.parse.commands;

import at.fhjoanneum.ippr.communicator.parser.ParseResult;

public class StoreInternalDataCommand {

  private final Long id;
  private final Long configId;
  private final ParseResult parseResult;

  public StoreInternalDataCommand(final Long id, final Long configId,
      final ParseResult parseResult) {
    this.id = id;
    this.configId = configId;
    this.parseResult = parseResult;
  }

  public Long getId() {
    return id;
  }

  public Long getConfigId() {
    return configId;
  }

  public ParseResult getParseResult() {
    return parseResult;
  }
}
