package at.fhjoanneum.ippr.gateway.security.usermapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserGroupMappingRunner implements CommandLineRunner {


  @Autowired
  private UserGroupMappingService mappingService;

  @Override
  public void run(final String... args) throws Exception {
    mappingService.mapUsers();
  }

}
