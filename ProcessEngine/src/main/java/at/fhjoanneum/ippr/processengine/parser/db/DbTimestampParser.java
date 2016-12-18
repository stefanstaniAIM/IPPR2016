package at.fhjoanneum.ippr.processengine.parser.db;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class DbTimestampParser implements DbParser<LocalDateTime> {

  private final static DateTimeFormatter FORMAT =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

  @Override
  public String parse(final LocalDateTime value) {
    return value.format(FORMAT);
  }

}
