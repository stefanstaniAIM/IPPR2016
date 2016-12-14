package at.fhjoanneum.ippr.processengine.parser;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

@Component
public class TimestampParser implements Parser<LocalDateTime> {

  @Override
  public LocalDateTime parse(final String value) {

    return LocalDateTime.ofInstant(Instant.ofEpochMilli(NumberUtils.createLong(value)),
        ZoneId.systemDefault());
  }
}
