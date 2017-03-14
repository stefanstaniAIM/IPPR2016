package at.fhjoanneum.ippr.communicator.persistence.entities.datatypecomposer;

import java.util.Objects;

import at.fhjoanneum.ippr.communicator.persistence.entities.Builder;
import at.fhjoanneum.ippr.communicator.persistence.objects.DataType;
import at.fhjoanneum.ippr.communicator.persistence.objects.datatypecomposer.DataTypeComposer;

public class DataTypeComposerBuilder implements Builder<DataTypeComposer> {

  private DataType dataType;
  private String composerClass;
  private String description;

  public DataTypeComposerBuilder dataType(final DataType dataType) {
    Objects.requireNonNull(dataType);
    this.dataType = dataType;
    return this;
  }

  public DataTypeComposerBuilder composerClass(final String composerClass) {
    Objects.requireNonNull(composerClass);
    this.composerClass = composerClass;
    return this;
  }

  public DataTypeComposerBuilder description(final String description) {
    Objects.requireNonNull(description);
    this.description = description;
    return this;
  }

  @Override
  public DataTypeComposer build() {
    Objects.requireNonNull(dataType);
    Objects.requireNonNull(composerClass);
    return new DataTypeComposerImpl(dataType, composerClass, description);
  }

}
