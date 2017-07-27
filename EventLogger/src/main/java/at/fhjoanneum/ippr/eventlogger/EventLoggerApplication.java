package at.fhjoanneum.ippr.eventlogger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
@EntityScan("at.fhjoanneum.ippr.eventlogger.persistence")
public class EventLoggerApplication {

  private final static Logger LOG = LoggerFactory.getLogger(EventLoggerApplication.class);

  public static void main(final String[] args) {
    SpringApplication.run(EventLoggerApplication.class, args);
  }
}
