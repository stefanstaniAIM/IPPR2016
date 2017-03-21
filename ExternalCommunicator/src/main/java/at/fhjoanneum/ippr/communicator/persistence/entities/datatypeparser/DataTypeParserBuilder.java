package at.fhjoanneum.ippr.communicator.persistence.entities.datatypeparser;

import java.util.Objects;

import at.fhjoanneum.ippr.communicator.persistence.entities.Builder;
import at.fhjoanneum.ippr.communicator.persistence.objects.DataType;
import at.fhjoanneum.ippr.communicator.persistence.objects.datatypeparser.DataTypeParser;

public class DataTypeParserBuilder implements Builder<DataTypeParser> {

  private DataType dataType;
  private String parserClass;
  private String description;

  public DataTypeParserBuilder dataType(final DataType dataType) {
    Objects.requireNonNull(dataType);
    this.dataType = dataType;
    return this;
  }

  public DataTypeParserBuilder parserClass(final String parserClass) {
    Objects.requireNonNull(parserClass);
    this.parserClass = parserClass;
    return this;
  }

  public DataTypeParserBuilder description(final String description) {
    Objects.requireNonNull(description);
    this.description = description;
    return this;
  }

  @Override
  public DataTypeParser build() {
    Objects.requireNonNull(dataType);
    Objects.requireNonNull(parserClass);
    return new DataTypeParserImpl(dataType, parserClass, description);
  }

}
