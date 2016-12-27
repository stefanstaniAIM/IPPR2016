package at.fhjoanneum.ippr.processengine.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import at.fhjoanneum.ippr.persistence.objects.model.enums.FieldType;
import at.fhjoanneum.ippr.processengine.parser.db.DbParser;
import at.fhjoanneum.ippr.processengine.parser.db.DbParserAllocation;
import at.fhjoanneum.ippr.processengine.parser.json.JsonParser;
import at.fhjoanneum.ippr.processengine.parser.json.JsonParserAllocation;

@Component
public class DbValueParser {

  @Autowired
  private JsonParserAllocation jsonParserAllocation;

  @Autowired
  private DbParserAllocation dbParserAllocation;

  public String parse(final String value, final FieldType fieldType) {
    switch (fieldType) {
      case DATE:
        final JsonParser<LocalDate> dateParser = jsonParserAllocation.getParser(fieldType);
        throw new UnsupportedOperationException();
      case DECIMAL:
        final JsonParser<Float> decimalParser = jsonParserAllocation.getParser(fieldType);
        throw new UnsupportedOperationException();
      case NUMBER:
        final JsonParser<Integer> numberParser = jsonParserAllocation.getParser(fieldType);
        final DbParser<Integer> dbNumberParser = dbParserAllocation.getParser(fieldType);
        return dbNumberParser.parse(numberParser.parse(value));
      case STRING:
        final JsonParser<String> stringParser = jsonParserAllocation.getParser(fieldType);
        final DbParser<String> dbStringParser = dbParserAllocation.getParser(fieldType);
        return dbStringParser.parse(stringParser.parse(value));
      case TIMESTAMP:
        final JsonParser<LocalDateTime> timestampParser = jsonParserAllocation.getParser(fieldType);
        final DbParser<LocalDateTime> dbTimestampParser = dbParserAllocation.getParser(fieldType);
        return dbTimestampParser.parse(timestampParser.parse(value));
      default:
        throw new IllegalArgumentException("Could not find parser");
    }
  }

  public boolean canParse(final String value, final FieldType fieldType) {
    return jsonParserAllocation.getParser(fieldType).canParse(value);
  }
}
