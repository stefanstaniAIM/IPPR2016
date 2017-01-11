package at.fhjoanneum.ippr.processengine.composer.json;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import at.fhjoanneum.ippr.persistence.objects.model.enums.FieldType;

@Configuration
public class JsonComposerAllocation {

  @Autowired
  private JsonDateComposer jsonDateComposer;
  @Autowired
  private JsonDecimalComposer jsonDecimalComposer;
  @Autowired
  private JsonNumberComposer jsonNumberComposer;
  @Autowired
  private JsonStringComposer jsonStringComposer;
  @Autowired
  private JsonTimestampComposer timestampComposer;

  public <T> JsonComposer<T> getComposer(final FieldType fieldType) {
    switch (fieldType) {
      case DATE:
        return (JsonComposer<T>) jsonDateComposer;
      case DECIMAL:
        return (JsonComposer<T>) jsonDecimalComposer;
      case NUMBER:
        return (JsonComposer<T>) jsonNumberComposer;
      case STRING:
        return (JsonComposer<T>) jsonStringComposer;
      case TIMESTAMP:
        return (JsonComposer<T>) timestampComposer;
      default:
        throw new IllegalArgumentException("Could not find json composer");
    }
  }
}
