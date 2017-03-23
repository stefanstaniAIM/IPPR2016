package at.fhjoanneum.ippr.communicator.composer.datatype;

public class IntComposer implements DataTypeComposer {

  @Override
  public String compose(final String input) {
    return input.toString();
  }
}
