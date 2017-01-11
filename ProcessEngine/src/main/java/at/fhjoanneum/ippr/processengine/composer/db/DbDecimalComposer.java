package at.fhjoanneum.ippr.processengine.composer.db;

import java.text.ParseException;

import org.springframework.stereotype.Component;

import at.fhjoanneum.ippr.processengine.config.FormatConfig;

@Component
public class DbDecimalComposer implements DbComposer<Float> {

  @Override
  public Float compose(final String value) {
    try {
      return FormatConfig.DECIMAL_FORMAT.parse(value).floatValue();
    } catch (final ParseException e) {
      return null;
    }
  }

  @Override
  public boolean canCompose(final String value) {
    try {
      FormatConfig.DECIMAL_FORMAT.parse(value).floatValue();
      return true;
    } catch (final Exception e) {
      return false;
    }
  }

}
