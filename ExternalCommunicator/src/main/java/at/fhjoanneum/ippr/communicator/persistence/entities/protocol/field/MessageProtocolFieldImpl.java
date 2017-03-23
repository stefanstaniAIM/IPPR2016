package at.fhjoanneum.ippr.communicator.persistence.entities.protocol.field;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import at.fhjoanneum.ippr.communicator.persistence.entities.protocol.MessageProtocolImpl;
import at.fhjoanneum.ippr.communicator.persistence.objects.DataType;
import at.fhjoanneum.ippr.communicator.persistence.objects.protocol.MessageProtocol;
import at.fhjoanneum.ippr.communicator.persistence.objects.protocol.MessageProtocolField;

@Entity(name = "MESSAGE_PROTOCOL_FIELD")
public class MessageProtocolFieldImpl implements MessageProtocolField, Serializable {

  private static final long serialVersionUID = -488915254635654609L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "message_protocol_id", referencedColumnName = "id")
  private MessageProtocolImpl messageProtocol;

  @Column
  private String externalName;

  @Column
  private String internalName;

  @Column
  @Enumerated(EnumType.STRING)
  private DataType dataType;

  @Column
  private boolean mandatory;

  @Column
  private String defaultValue;

  MessageProtocolFieldImpl() {}

  MessageProtocolFieldImpl(final MessageProtocolImpl messageProtocol, final String externalName,
      final String internalName, final DataType dataType, final boolean mandatory,
      final String defaultValue) {
    this.messageProtocol = messageProtocol;
    this.externalName = externalName;
    this.internalName = internalName;
    this.dataType = dataType;
    this.mandatory = mandatory;
    this.defaultValue = defaultValue;
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public MessageProtocol getMessageProtocol() {
    return messageProtocol;
  }

  @Override
  public String getExternalName() {
    return externalName;
  }

  @Override
  public String getInternalName() {
    return internalName;
  }

  @Override
  public DataType getDataType() {
    return dataType;
  }

  @Override
  public boolean isMandatory() {
    return mandatory;
  }

  @Override
  public String getDefaultValue() {
    return defaultValue;
  }



  @Override
  public String toString() {
    return "MessageProtocolField [id=" + id + ", externalName=" + externalName + ", internalName="
        + internalName + ", dataType=" + dataType + ", mandatory=" + mandatory + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
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
    final MessageProtocolFieldImpl other = (MessageProtocolFieldImpl) obj;
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
