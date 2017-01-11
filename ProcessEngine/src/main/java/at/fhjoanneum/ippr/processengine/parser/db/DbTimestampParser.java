package at.fhjoanneum.ippr.processengine.parser.db;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import at.fhjoanneum.ippr.processengine.config.FormatConfig;

@Component
public class DbTimestampParser implements DbParser<LocalDateTime> {

  @Override
  public String parse(final LocalDateTime value) {
    return value.format(FormatConfig.TIMESTAMP_FORMAT);
  }

}
