package at.fhjoanneum.ippr.communicator.composer;

public class IntComposer implements Composer<Integer> {

  @Override
  public String compose(final Integer input) {
    return input.toString();
  }
}
