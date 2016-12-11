package at.fhjoanneum.ippr.processengine.parser;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import org.apache.commons.lang3.math.NumberUtils;

public class DateParser implements Parser<LocalDate> {

  @Override
  public LocalDate parse(final String value) {
    return Instant.ofEpochMilli(NumberUtils.createLong(value)).atZone(ZoneId.systemDefault())
        .toLocalDate();
  }

}
