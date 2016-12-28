package at.fhjoanneum.ippr.processengine.composer.db;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

@Component
public class DbNumberComposer implements DbComposer<Integer> {

  @Override
  public Integer compose(final String value) {
    return NumberUtils.createInteger(value);
  }

  @Override
  public boolean canCompose(final String value) {
    try {
      NumberUtils.createInteger(value);
      return true;
    } catch (final Exception e) {
      return false;
    }
  }

}
