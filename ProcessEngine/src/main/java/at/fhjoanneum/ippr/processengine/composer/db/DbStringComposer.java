package at.fhjoanneum.ippr.processengine.composer.db;

import org.springframework.stereotype.Component;

@Component
public class DbStringComposer implements DbComposer<String> {

  @Override
  public String compose(final String value) {
    return value;
  }

  @Override
  public boolean canCompose(final String value) {
    return true;
  }

}
