package at.fhjoanneum.ippr.communicator.persistence.entities.config;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import at.fhjoanneum.ippr.communicator.persistence.entities.basic.outbound.BasicOutboundConfigurationImpl;
import at.fhjoanneum.ippr.communicator.persistence.objects.basic.outbound.BasicOutboundConfiguration;

@Entity(name = "OUTBOUND_CONFIGURATION_MAP")
public class OutboundConfigurationMap implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotNull
  @Column(unique = true)
  private Long messageFlowId;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "CONFIG_ID")
  private BasicOutboundConfigurationImpl outboundConfiguration;

  OutboundConfigurationMap() {}

  OutboundConfigurationMap(final Long messageFlowId,
      final BasicOutboundConfigurationImpl outboundConfiguration) {
    this.messageFlowId = messageFlowId;
    this.outboundConfiguration = outboundConfiguration;
  }

  public Long getId() {
    return id;
  }

  public Long getMessageFlowId() {
    return messageFlowId;
  }

  public BasicOutboundConfiguration getOutboundConfiguration() {
    return outboundConfiguration;
  }

  @Override
  public String toString() {
    return "OutboundConfigurationMap [id=" + id + ", messageFlowId=" + messageFlowId
        + ", outboundConfiguration=" + outboundConfiguration + "]";
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
    final OutboundConfigurationMap other = (OutboundConfigurationMap) obj;
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
