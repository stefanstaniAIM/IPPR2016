package at.fhjoanneum.ippr.communicator.persistence.entities.basic.email;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;

import at.fhjoanneum.ippr.communicator.persistence.entities.basic.AbstractBasicConfiguration;
import at.fhjoanneum.ippr.communicator.persistence.entities.datatypecomposer.DataTypeComposerImpl;
import at.fhjoanneum.ippr.communicator.persistence.entities.protocol.MessageProtocolImpl;
import at.fhjoanneum.ippr.communicator.persistence.objects.DataType;
import at.fhjoanneum.ippr.communicator.persistence.objects.basic.EmailConfiguration;

@Entity(name = "EMAIL_CONFIGURATION")
public class EmailConfigurationImpl extends AbstractBasicConfiguration
    implements EmailConfiguration {

  private static final long serialVersionUID = 4093767496155709498L;

  @Column
  private final String email;

  EmailConfigurationImpl(final String name, final Map<DataType, DataTypeComposerImpl> composer,

      final MessageProtocolImpl messageProtocol, final String email) {
    super(name, composer, messageProtocol);
    this.email = email;
  }

  @Override
  public String getEmail() {
    return email;
  }

}
