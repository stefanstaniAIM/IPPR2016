package at.fhjoanneum.ippr.processengine.composer.json;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.stereotype.Component;

@Component
public class JsonTimestampComposer implements JsonComposer<LocalDateTime> {

  @Override
  public String compose(final LocalDateTime value) {
    return String.valueOf(value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
  }


}
