package at.fhjoanneum.ippr.communicator.persistence.entities.datatypecomposer;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import at.fhjoanneum.ippr.communicator.persistence.objects.DataType;
import at.fhjoanneum.ippr.communicator.persistence.objects.datatypecomposer.DataTypeComposer;

@Entity(name = "DATA_TYPE_COMPOSER")
public class DataTypeComposerImpl implements DataTypeComposer, Serializable {

  private static final long serialVersionUID = 8695932753032054228L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column
  @Enumerated(EnumType.STRING)
  private DataType dataType;

  @Column
  private String composerClass;

  @Column
  private String description;

  DataTypeComposerImpl() {}

  DataTypeComposerImpl(final DataType dataType, final String composerClass) {
    this.dataType = dataType;
    this.composerClass = composerClass;
  }

  DataTypeComposerImpl(final DataType dataType, final String composerClass,
      final String description) {
    this(dataType, composerClass);
    this.description = description;
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public DataType getDataType() {
    return dataType;
  }

  @Override
  public String getComposerClass() {
    return composerClass;
  }


  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public String toString() {
    return "DataTypeParser [dataType=" + dataType + ", composerClass=" + composerClass + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((dataType == null) ? 0 : dataType.hashCode());
    result = prime * result + ((composerClass == null) ? 0 : composerClass.hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final DataTypeComposerImpl other = (DataTypeComposerImpl) obj;
    if (dataType != other.dataType) {
      return false;
    }
    if (composerClass == null) {
      if (other.composerClass != null) {
        return false;
      }
    } else if (!composerClass.equals(other.composerClass)) {
      return false;
    }
    return true;
  }
}
