package at.fhjoanneum.ippr.communicator.composer.datatype;

public class IntComposer implements DataTypeComposer<Integer> {

  @Override
  public String compose(final Integer input) {
    return input.toString();
  }
}
