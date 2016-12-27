package at.fhjoanneum.ippr.processengine.parser.json;

import org.springframework.stereotype.Component;

@Component
public class JsonStringParser implements JsonParser<String> {

  @Override
  public boolean canParse(final String value) {
    return true;
  }

  @Override
  public String parse(final String value) {
    return value;
  }

}
