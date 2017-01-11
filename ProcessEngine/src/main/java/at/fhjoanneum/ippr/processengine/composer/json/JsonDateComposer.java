package at.fhjoanneum.ippr.processengine.composer.json;

import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.stereotype.Component;

@Component
public class JsonDateComposer implements JsonComposer<LocalDate> {

  @Override
  public String compose(final LocalDate value) {
    return String.valueOf(value.atStartOfDay(ZoneId.systemDefault()).toEpochSecond());
  }

}
