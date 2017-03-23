package at.fhjoanneum.ippr.communicator.parser.datatype;

import at.fhjoanneum.ippr.communicator.persistence.objects.DataType;

public final class ParserUtils {

  private ParserUtils() {}

  public static String parse(final String input, final DataType dataType) {
    switch (dataType) {
      case STRING:
        return new StringParser().parse(input);
      default:
        throw new IllegalArgumentException("Could not find parser for [" + dataType + "]");
    }
  }
}
