package at.fhjoanneum.ippr.communicator.persistence.entities.basic.inbound;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;

import com.google.common.collect.ImmutableMap;

import at.fhjoanneum.ippr.communicator.persistence.entities.datatypeparser.DataTypeParserImpl;
import at.fhjoanneum.ippr.communicator.persistence.entities.protocol.MessageProtocolImpl;
import at.fhjoanneum.ippr.communicator.persistence.objects.DataType;
import at.fhjoanneum.ippr.communicator.persistence.objects.basic.inbound.BasicInboundConfiguration;
import at.fhjoanneum.ippr.communicator.persistence.objects.datatypeparser.DataTypeParser;
import at.fhjoanneum.ippr.communicator.persistence.objects.protocol.MessageProtocol;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class AbstractBasicInboundConfiguration implements BasicInboundConfiguration, Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column
  private String name;

  @ManyToOne
  private MessageProtocolImpl messageProtocol;

  @Column
  private String parserClass;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "basic_configuration_parser_map",
      joinColumns = {@JoinColumn(name = "abstract_basic_configuration_id")},
      inverseJoinColumns = {@JoinColumn(name = "parser_id")})
  @MapKey
  private final Map<DataType, DataTypeParserImpl> parser = new HashMap<>();

  protected AbstractBasicInboundConfiguration() {}

  protected AbstractBasicInboundConfiguration(final String name,
      final MessageProtocolImpl messageProtocol, final String parserClass) {
    this.name = name;
    this.messageProtocol = messageProtocol;
    this.parserClass = parserClass;
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public MessageProtocol getMessageProtocol() {
    return messageProtocol;
  }

  @Override
  public String getParserClass() {
    return parserClass;
  }

  @Override
  public Map<DataType, DataTypeParser> getDataTypeParser() {
    return ImmutableMap.copyOf(parser);
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
    final AbstractBasicInboundConfiguration other = (AbstractBasicInboundConfiguration) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "AbstractBasicInboundConfiguration [id=" + id + ", name=" + name + ", messageProtocol="
        + messageProtocol + ", parserClass=" + parserClass + ", parser=" + parser + "]";
  }
}
