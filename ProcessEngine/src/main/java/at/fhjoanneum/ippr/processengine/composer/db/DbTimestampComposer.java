package at.fhjoanneum.ippr.processengine.composer.db;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class DbTimestampComposer implements DbComposer<LocalDateTime> {

  private final static DateTimeFormatter FORMAT =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

  @Override
  public LocalDateTime compose(final String value) {
    return LocalDateTime.parse(value, FORMAT);
  }

  @Override
  public boolean canCompose(final String value) {
    try {
      LocalDateTime.parse(value, FORMAT);
      return true;
    } catch (final Exception e) {
      return false;
    }
  }

}
