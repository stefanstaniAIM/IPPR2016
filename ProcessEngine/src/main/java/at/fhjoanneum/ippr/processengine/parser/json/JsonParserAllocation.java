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


  public JsonParser<?> getParser(final FieldType fieldType) {
    switch (fieldType) {
      case DATE:
        return dateParser;
      case DECIMAL:
        return decimalParser;
      case NUMBER:
        return numberParser;
      case STRING:
        return stringParser;
      case TIMESTAMP:
        return timestampParser;
      default:
        throw new IllegalArgumentException("Could not find parser");
    }
  }

}
