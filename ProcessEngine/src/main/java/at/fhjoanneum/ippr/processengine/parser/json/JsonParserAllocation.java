package at.fhjoanneum.ippr.processengine.parser.json;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import at.fhjoanneum.ippr.persistence.objects.model.enums.FieldType;

@Configuration
public class JsonParserAllocation {

  @Autowired
  private JsonDateParser dateParser;
  @Autowired
  private JsonDecimalParser decimalParser;
  @Autowired
  private JsonNumberParser numberParser;
  @Autowired
  private JsonStringParser stringParser;
  @Autowired
  private JsonTimestampParser timestampParser;


  public <T> JsonParser<T> getParser(final FieldType fieldType) {
    switch (fieldType) {
      case DATE:
        return (JsonParser<T>) dateParser;
      case DECIMAL:
        return (JsonParser<T>) decimalParser;
      case NUMBER:
        return (JsonParser<T>) numberParser;
      case STRING:
        return (JsonParser<T>) stringParser;
      case TIMESTAMP:
        return (JsonParser<T>) timestampParser;
      default:
        throw new IllegalArgumentException("Could not find parser");
    }
  }

}
