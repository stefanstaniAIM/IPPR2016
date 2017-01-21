package at.fhjoanneum.ippr.gateway.security.rbacmapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RBACMappingRunner implements CommandLineRunner {


  @Autowired
  private RBACMappingService mappingService;

  @Override
  public void run(final String... args) throws Exception {
    mappingService.mapUsers();
  }

}
