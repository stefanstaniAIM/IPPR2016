package at.fhjoanneum.ippr.commons.dto.processengine.user;

public interface UserContainer {

  Long getUserId();

  UserDTO getUser();

  void appendUser(final UserDTO user);
}
