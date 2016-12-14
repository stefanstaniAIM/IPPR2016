package at.fhjoanneum.ippr.processengine.parser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import at.fhjoanneum.ippr.persistence.objects.model.enums.FieldType;

@Configuration
public class ParserAllocation {

  @Autowired
  private DateParser dateParser;
  @Autowired
  private DecimalParser decimalParser;
  @Autowired
  private NumberParser numberParser;
  @Autowired
  private StringParser stringParser;
  @Autowired
  private TimestampParser timestampParser;


  public Parser<?> getParser(final FieldType fieldType) {
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
