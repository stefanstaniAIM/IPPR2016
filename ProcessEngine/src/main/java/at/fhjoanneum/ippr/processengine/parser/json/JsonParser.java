package at.fhjoanneum.ippr.processengine.parser.json;

public interface JsonParser<T> {

  /**
   * Parses a String to a given type.
   *
   * @param value the value to parse
   * @return the parsed value
   */
  T parse(String value);

  boolean canParse(String value);
}
