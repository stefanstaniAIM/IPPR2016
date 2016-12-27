package at.fhjoanneum.ippr.processengine.parser.db;

public interface DbParser<T> {

  /**
   * Converts the value that is generic to a String, that can be stored in database.
   *
   * @param jsonParsed the value to parse
   * @return the string represenation of the value
   */
  String parse(T jsonParsed);
}
