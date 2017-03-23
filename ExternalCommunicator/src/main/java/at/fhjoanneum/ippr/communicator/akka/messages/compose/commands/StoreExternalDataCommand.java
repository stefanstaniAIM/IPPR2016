package at.fhjoanneum.ippr.communicator.akka.messages.compose.commands;

public class StoreExternalDataCommand {

  private final Long id;
  private final String data;

  public StoreExternalDataCommand(final Long id, final String data) {
    this.id = id;
    this.data = data;
  }

  public Long getId() {
    return id;
  }

  public String getData() {
    return data;
  }
}
