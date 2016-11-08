package at.fhjoanneum.ippr.gateway.security.usermapping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserGroupMappingRunner implements CommandLineRunner {

  private static final Logger LOG = LogManager.getLogger(UserGroupMappingRunner.class);

  @Autowired
  private UserGroupMappingService mappingService;

  @Override
  public void run(final String... args) throws Exception {
    mappingService.mapUsers();
  }

}
