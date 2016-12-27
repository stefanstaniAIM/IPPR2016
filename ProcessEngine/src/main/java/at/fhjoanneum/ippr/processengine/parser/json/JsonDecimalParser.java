package at.fhjoanneum.ippr.processengine.parser.json;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

@Component
public class JsonDecimalParser implements JsonParser<Float> {

  @Override
  public boolean canParse(final String value) {
    try {
      NumberUtils.createFloat(value);
      return true;
    } catch (final Exception e) {
      return false;
    }
  }

  @Override
  public Float parse(final String value) {
    return NumberUtils.createFloat(value);
  }
}
