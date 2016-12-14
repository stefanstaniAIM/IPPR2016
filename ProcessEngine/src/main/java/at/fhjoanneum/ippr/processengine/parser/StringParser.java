package at.fhjoanneum.ippr.processengine.parser;

import org.springframework.stereotype.Component;

@Component
public class StringParser implements Parser<String> {

  @Override
  public String parse(final String value) {
    return value;
  }

}
