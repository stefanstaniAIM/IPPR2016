package at.fhjoanneum.ippr.processengine.parser.db;

import org.springframework.stereotype.Component;

@Component
public class DbNumberParser implements DbParser<Integer> {

  @Override
  public String parse(final Object value) {
    final Integer parsed = (Integer) value;
    return String.valueOf(parsed.intValue());
  }

}
