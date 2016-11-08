package at.fhjoanneum.ippr.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(considerNestedRepositories = true)
public class GatewayApplication {

  public static void main(final String[] args) {
    SpringApplication.run(GatewayApplication.class, args);
  }
}
