package at.fhjoanneum.ippr.communicator.parser.datatype;

public class StringParser implements DataTypeParser {

  @Override
  public String parse(final String input) {
    return input;
  }

  @Override
  public String getDescription() {
    return "Standard parser for string";
  }

}
