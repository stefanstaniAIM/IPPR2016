package at.fhjoanneum.ippr.communicator.composer.datatype;

public class StringComposer implements DataTypeComposer<String> {

  @Override
  public String compose(final String input) {
    return input;
  }
}
