package at.fhjoanneum.ippr.processengine.parser;

public interface Parser<T> {

  /**
   * Parses a String to a given type.
   *
   * @param value the value to parse
   * @return the parsed value
   */
  T parse(String value);
}
