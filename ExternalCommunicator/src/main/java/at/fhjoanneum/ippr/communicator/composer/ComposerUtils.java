package at.fhjoanneum.ippr.communicator.composer;

public class ComposerUtils {

  public String compose(final Object input) {
    if (input instanceof Integer) {
      return new IntComposer().compose((Integer) input);
    } else if (input instanceof String) {
      return new StringComposer().compose((String) input);
    } else {
      throw new IllegalArgumentException("Could not find composer for [" + input + "]");
    }
  }
}
