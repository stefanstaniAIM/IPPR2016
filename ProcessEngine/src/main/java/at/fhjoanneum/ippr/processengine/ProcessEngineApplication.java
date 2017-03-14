package at.fhjoanneum.ippr.processengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication
@EntityScan("at.fhjoanneum.ippr.persistence.entities")
@EnableDiscoveryClient
@EnableFeignClients
public class ProcessEngineApplication {

  public static void main(final String[] args) {
    SpringApplication.run(ProcessEngineApplication.class, args);
  }
}
