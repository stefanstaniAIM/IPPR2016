package at.fhjoanneum.ippr.processengine.parser.db;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import at.fhjoanneum.ippr.processengine.config.FormatConfig;

@Component
public class DbDateParser implements DbParser<LocalDate> {

  @Override
  public String parse(final LocalDate jsonParsed) {
    return jsonParsed.format(FormatConfig.DATE_FORMAT);
  }

}
