package at.fhjoanneum.ippr.processengine.parser.json;

import org.springframework.stereotype.Component;

@Component
public class JsonStringParser implements JsonParser<String> {

  @Override
  public String parse(final String value) {
    return value;
  }

}
