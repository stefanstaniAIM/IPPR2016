package at.fhjoanneum.ippr.processengine.composer.json;

import org.springframework.stereotype.Component;

@Component
public class JsonStringComposer implements JsonComposer<String> {

  @Override
  public String compose(final String value) {
    return value;
  }
}
