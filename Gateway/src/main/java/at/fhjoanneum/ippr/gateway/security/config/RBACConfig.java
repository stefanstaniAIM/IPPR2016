package at.fhjoanneum.ippr.gateway.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import at.fhjoanneum.ippr.gateway.security.authentication.AuthenticationService;
import at.fhjoanneum.ippr.gateway.security.authentication.memory.AuthenticationServiceMemoryImpl;
import at.fhjoanneum.ippr.gateway.security.rbacmapping.retrieval.RBACRetrievalService;
import at.fhjoanneum.ippr.gateway.security.rbacmapping.retrieval.memory.RBACMemoryCondition;
import at.fhjoanneum.ippr.gateway.security.rbacmapping.retrieval.memory.RBACRetrievalServiceMemoryImpl;

@Configuration
public class RBACConfig {

  @Bean(name = "rbacRetrievalService")
  @Conditional(RBACMemoryCondition.class)
  public RBACRetrievalService userGroupMemoryService() {
    return new RBACRetrievalServiceMemoryImpl();
  }

  @Bean(name = "authenticationService")
  @Conditional(RBACMemoryCondition.class)
  public AuthenticationService memoryAuthenticationService() {
    return new AuthenticationServiceMemoryImpl();
  }
}
