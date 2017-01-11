package at.fhjoanneum.ippr.processengine.composer.json;

import org.springframework.stereotype.Component;

import at.fhjoanneum.ippr.processengine.config.FormatConfig;

@Component
public class JsonDecimalComposer implements JsonComposer<Float> {

  @Override
  public String compose(final Float value) {
    return FormatConfig.DECIMAL_FORMAT.format(value);
  }

}
