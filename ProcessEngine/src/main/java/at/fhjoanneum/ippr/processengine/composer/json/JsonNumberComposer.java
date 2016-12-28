package at.fhjoanneum.ippr.processengine.composer.json;

import org.springframework.stereotype.Component;

@Component
public class JsonNumberComposer implements JsonComposer<Integer> {

  @Override
  public String compose(final Integer value) {
    return String.valueOf(value);
  }

}
