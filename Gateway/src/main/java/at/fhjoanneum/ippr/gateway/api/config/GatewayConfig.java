package at.fhjoanneum.ippr.gateway.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GatewayConfig {

  @Value("${ipconfig.pmstorage}")
  private String processModelStorageAddress;

  public String getProcessModelStorageAddress() {
    return processModelStorageAddress;
  }
}
