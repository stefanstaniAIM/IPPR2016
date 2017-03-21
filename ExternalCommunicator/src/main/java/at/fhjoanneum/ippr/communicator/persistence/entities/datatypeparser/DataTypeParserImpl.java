package at.fhjoanneum.ippr.communicator.persistence.entities.datatypeparser;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import at.fhjoanneum.ippr.communicator.persistence.objects.DataType;
import at.fhjoanneum.ippr.communicator.persistence.objects.datatypeparser.DataTypeParser;

@Entity(name = "DATA_TYPE_PARSER")
public class DataTypeParserImpl implements DataTypeParser, Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column
  @Enumerated(EnumType.STRING)
  private DataType dataType;

  @Column
  private String parserClass;

  @Column
  private String description;

  DataTypeParserImpl() {}

  DataTypeParserImpl(final DataType dataType, final String parserClass, final String description) {
    this.dataType = dataType;
    this.parserClass = parserClass;
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
  public String getParserClass() {
    return parserClass;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public String toString() {
    return "DataTypeParserImpl [id=" + id + ", dataType=" + dataType + ", parserClass="
        + parserClass + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final DataTypeParserImpl other = (DataTypeParserImpl) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

}
