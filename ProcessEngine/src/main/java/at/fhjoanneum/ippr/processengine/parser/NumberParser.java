package at.fhjoanneum.ippr.processengine.parser;

import org.apache.commons.lang3.math.NumberUtils;

public class NumberParser implements Parser<Integer> {

  @Override
  public Integer parse(final String value) {
    return NumberUtils.createInteger(value);
  }

}
