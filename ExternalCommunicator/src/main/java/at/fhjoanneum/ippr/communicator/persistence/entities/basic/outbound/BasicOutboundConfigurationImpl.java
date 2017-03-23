package at.fhjoanneum.ippr.communicator.persistence.entities.basic.outbound;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
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
import javax.persistence.MapKeyColumn;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import at.fhjoanneum.ippr.communicator.persistence.entities.datatypecomposer.DataTypeComposerImpl;
import at.fhjoanneum.ippr.communicator.persistence.entities.protocol.MessageProtocolImpl;
import at.fhjoanneum.ippr.communicator.persistence.objects.DataType;
import at.fhjoanneum.ippr.communicator.persistence.objects.basic.outbound.BasicOutboundConfiguration;
import at.fhjoanneum.ippr.communicator.persistence.objects.datatypecomposer.DataTypeComposer;
import at.fhjoanneum.ippr.communicator.persistence.objects.protocol.MessageProtocol;

@Entity(name = "BASIC_OUTBOUND_CONFIGURATION")
@Inheritance(strategy = InheritanceType.JOINED)
public class BasicOutboundConfigurationImpl implements BasicOutboundConfiguration, Serializable {

  private static final long serialVersionUID = -7550233823589354342L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column
  private String name;

  @ManyToOne
  private MessageProtocolImpl messageProtocol;

  @Column
  private String composerClass;

  @Column
  private String sendPlugin;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "basic_configuration_composer_map",
      joinColumns = {@JoinColumn(name = "basic_configuration_id")},
      inverseJoinColumns = {@JoinColumn(name = "composer_id")})
  @MapKey
  private Map<DataType, DataTypeComposerImpl> composer = Maps.newHashMap();

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "BASIC_OUTBOUND_CONFIGURATION_MAP",
      joinColumns = @JoinColumn(name = "basic_configuration_id"))
  @MapKeyColumn(name = "CONFIG_KEY")
  @Column(name = "VALUE")
  private Map<String, String> configuration = new HashMap<>();

  BasicOutboundConfigurationImpl() {}

  BasicOutboundConfigurationImpl(final String name, final MessageProtocolImpl messageProtocol,
      final String composerClass, final String sendPlugin,
      final Map<DataType, DataTypeComposerImpl> composer, final Map<String, String> configuration) {
    this.name = name;
    this.messageProtocol = messageProtocol;
    this.composerClass = composerClass;
    this.sendPlugin = sendPlugin;
    this.composer = composer;
    this.configuration = configuration;
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
  public String getComposerClass() {
    return composerClass;
  }

  @Override
  public String getSendPlugin() {
    return sendPlugin;
  }

  @Override
  public Map<DataType, DataTypeComposer> getDataTypeComposer() {
    return ImmutableMap.copyOf(composer);
  }

  @Override
  public Map<String, String> getConfiguration() {
    return ImmutableMap.copyOf(configuration);
  }

  @Override
  public Optional<String> getConfigurationEntry(final String key) {
    return Optional.ofNullable(configuration.get(key));
  }

  @Override
  public MessageProtocol getMessageProtocol() {
    return messageProtocol;
  }

  @Override
  public String toString() {
    return "AbstractBasicConfiguration [id=" + id + ", name=" + name + "]";
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
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final BasicOutboundConfigurationImpl other = (BasicOutboundConfigurationImpl) obj;
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    return true;
  }
}
