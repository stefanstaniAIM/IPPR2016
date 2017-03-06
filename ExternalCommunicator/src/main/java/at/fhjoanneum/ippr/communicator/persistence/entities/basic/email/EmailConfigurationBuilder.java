package at.fhjoanneum.ippr.communicator.persistence.entities.basic.email;

import java.util.Objects;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import com.google.common.base.Preconditions;

import at.fhjoanneum.ippr.communicator.persistence.entities.Builder;
import at.fhjoanneum.ippr.communicator.persistence.entities.basic.AbstractBasicConfigurationBuilder;
import at.fhjoanneum.ippr.communicator.persistence.objects.basic.EmailConfiguration;

public class EmailConfigurationBuilder extends AbstractBasicConfigurationBuilder
    implements Builder<EmailConfiguration> {

  private String email;

  public EmailConfigurationBuilder email(final String email) {
    Objects.requireNonNull(email);
    Preconditions.checkArgument(isValidEmailAddress(email));
    this.email = email;
    return this;
  }

  @Override
  public EmailConfiguration build() {
    Objects.requireNonNull(super.name);
    Preconditions.checkArgument(!super.composer.isEmpty());
    Objects.requireNonNull(messageProtocol);

    return new EmailConfigurationImpl(name, composer, messageProtocol, email);
  }

  private static boolean isValidEmailAddress(final String email) {
    boolean result = true;
    try {
      final InternetAddress emailAddr = new InternetAddress(email);
      emailAddr.validate();
    } catch (final AddressException ex) {
      result = false;
    }
    return result;
  }
}
