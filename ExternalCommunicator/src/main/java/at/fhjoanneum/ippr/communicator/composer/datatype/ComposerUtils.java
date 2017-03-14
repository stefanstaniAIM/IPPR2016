package at.fhjoanneum.ippr.communicator.composer.datatype;

import at.fhjoanneum.ippr.communicator.persistence.objects.DataType;

public class ComposerUtils {

  public static String compose(final Object input, final DataType datatype) {
    switch (datatype) {
      case STRING:
        return new StringComposer().compose((String) input);
      case INT:
        return new IntComposer().compose((Integer) input);
      default:
        throw new IllegalArgumentException("Could not find composer for [" + input + "]");
    }
  }
}
