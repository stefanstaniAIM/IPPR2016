package at.fhjoanneum.ippr.processengine.parser.json;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

@Component
public class JsonDateParser implements JsonParser<LocalDate> {

  @Override
  public boolean canParse(final String value) {
    try {
      Instant.ofEpochMilli(NumberUtils.createLong(value)).atZone(ZoneId.systemDefault())
          .toLocalDate();
      return true;
    } catch (final Exception e) {
      return false;
    }
  }

  @Override
  public LocalDate parse(final String value) {
    return Instant.ofEpochMilli(NumberUtils.createLong(value)).atZone(ZoneId.systemDefault())
        .toLocalDate();
  }

}
