package at.fhjoanneum.ippr.processengine.parser;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

@Component
public class DecimalParser implements Parser<Float> {

  @Override
  public Float parse(final String value) {
    return NumberUtils.createFloat(value);
  }
}
