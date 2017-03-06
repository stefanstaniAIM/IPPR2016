package at.fhjoanneum.ippr.communicator.composer;

public class StringComposer implements Composer<String> {

  @Override
  public String compose(final String input) {
    return input;
  }
}
