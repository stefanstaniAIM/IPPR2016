package at.fhjoanneum.ippr.processengine.parser.db;

import org.springframework.stereotype.Component;

import at.fhjoanneum.ippr.processengine.config.FormatConfig;

@Component
public class DbDecimalParser implements DbParser<Float> {


  @Override
  public String parse(final Float jsonParsed) {
    return FormatConfig.DECIMAL_FORMAT.format(jsonParsed);
  }

}
