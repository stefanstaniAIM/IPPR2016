package at.fhjoanneum.ippr.gateway.security.authentication;

import java.util.Optional;

import at.fhjoanneum.ippr.gateway.security.persistence.objects.User;

public interface AuthenticationService {

  Optional<User> authenticateUser(String username, String password);
}
