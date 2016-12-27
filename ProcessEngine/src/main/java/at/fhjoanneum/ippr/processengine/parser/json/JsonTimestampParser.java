package at.fhjoanneum.ippr.processengine.parser.json;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

@Component
public class JsonTimestampParser implements JsonParser<LocalDateTime> {

  @Override
  public boolean canParse(final String value) {
    try {
      LocalDateTime.ofInstant(Instant.ofEpochMilli(NumberUtils.createLong(value)),
          ZoneId.systemDefault());
      return true;
    } catch (final Exception e) {
      return false;
    }
  }

  @Override
  public LocalDateTime parse(final String value) {

    return LocalDateTime.ofInstant(Instant.ofEpochMilli(NumberUtils.createLong(value)),
        ZoneId.systemDefault());
  }
}
