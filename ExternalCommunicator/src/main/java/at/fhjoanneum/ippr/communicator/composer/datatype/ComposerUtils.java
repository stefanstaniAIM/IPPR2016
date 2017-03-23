package at.fhjoanneum.ippr.communicator.composer.datatype;

import at.fhjoanneum.ippr.communicator.persistence.objects.DataType;

public class ComposerUtils {

  public static String compose(final String input, final DataType datatype) {
    switch (datatype) {
      case STRING:
        return new StringComposer().compose(input);
      case INT:
        return new IntComposer().compose(input);
      default:
        throw new IllegalArgumentException("Could not find composer for [" + input + "]");
    }
  }
}
