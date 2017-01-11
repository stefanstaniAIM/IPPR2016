package at.fhjoanneum.ippr.processengine.composer.db;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import at.fhjoanneum.ippr.processengine.config.FormatConfig;

@Component
public class DbDateComposer implements DbComposer<LocalDate> {

  @Override
  public LocalDate compose(final String value) {
    return LocalDate.parse(value, FormatConfig.DATE_FORMAT);
  }

  @Override
  public boolean canCompose(final String value) {
    try {
      LocalDate.parse(value, FormatConfig.DATE_FORMAT);
      return true;
    } catch (final Exception e) {
      return false;
    }
  }

}
