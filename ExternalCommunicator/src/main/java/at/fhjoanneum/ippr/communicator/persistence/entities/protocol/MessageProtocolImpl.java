package at.fhjoanneum.ippr.communicator.persistence.entities.protocol;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import at.fhjoanneum.ippr.communicator.persistence.entities.protocol.field.MessageProtocolFieldImpl;
import at.fhjoanneum.ippr.communicator.persistence.objects.protocol.MessageProtocol;
import at.fhjoanneum.ippr.communicator.persistence.objects.protocol.MessageProtocolField;

@Entity(name = "MESSAGE_PROTOCOL")
public class MessageProtocolImpl implements MessageProtocol, Serializable {

  private static final long serialVersionUID = -488915254635624609L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column
  private String externalName;

  @Column
  private String internalName;

  @OneToMany(mappedBy = "messageProtocol", fetch = FetchType.EAGER)
  private final List<MessageProtocolFieldImpl> fields = Lists.newArrayList();

  @ManyToOne()
  @JoinColumn(name = "parent_bom_id")
  private MessageProtocolImpl parent;

  @OneToMany(mappedBy = "parent")
  @LazyCollection(LazyCollectionOption.FALSE)
  private final List<MessageProtocolImpl> children = Lists.newArrayList();

  MessageProtocolImpl() {}

  MessageProtocolImpl(final String externalName, final String internalName) {
    this.externalName = externalName;
    this.internalName = internalName;
  }

  MessageProtocolImpl(final MessageProtocolImpl parent, final String externalName,
      final String internalName) {
    this(externalName, internalName);
    this.parent = parent;
  }

  @Override
  public Long getId() {
    return id;
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
  public List<MessageProtocolField> getFields() {
    return ImmutableList.copyOf(fields);
  }

  @Override
  public List<MessageProtocol> getChildren() {
    return ImmutableList.copyOf(children);
  }

  @Override
  public MessageProtocol getParent() {
    return parent;
  }

  @Override
  public String toString() {
    return "MessageProtocol [id=" + id + ", externalName=" + externalName + ", internalName="
        + internalName + ", fields=" + fields + "]";
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
    final MessageProtocolImpl other = (MessageProtocolImpl) obj;
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
