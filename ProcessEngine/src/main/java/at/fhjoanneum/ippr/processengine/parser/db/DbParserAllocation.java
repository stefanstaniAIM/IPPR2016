package at.fhjoanneum.ippr.processengine.parser.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import at.fhjoanneum.ippr.persistence.objects.model.enums.FieldType;

@Configuration
public class DbParserAllocation {

  @Autowired
  private DbNumberParser dbNumberParser;
  @Autowired
  private DbStringParser dbStringParser;
  @Autowired
  private DbTimestampParser dbTimestampParser;

  public DbParser<?> getParser(final FieldType fieldType) {
    switch (fieldType) {
      case NUMBER:
        return dbNumberParser;
      case STRING:
        return dbStringParser;
      case TIMESTAMP:
        return dbTimestampParser;
      default:
        throw new IllegalArgumentException("Could not find parser");
    }
  }
}
