package at.fhjoanneum.ippr.processengine.parser.json;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

@Component
public class JsonNumberParser implements JsonParser<Integer> {

  @Override
  public boolean canParse(final String value) {
    try {
      NumberUtils.createInteger(value);
      return true;
    } catch (final Exception e) {
      return false;
    }
  }

  @Override
  public Integer parse(final String value) {
    return NumberUtils.createInteger(value);
  }

}
