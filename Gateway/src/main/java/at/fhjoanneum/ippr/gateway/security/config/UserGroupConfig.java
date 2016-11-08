package at.fhjoanneum.ippr.gateway.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import at.fhjoanneum.ippr.gateway.security.usermapping.retrieval.UserGroupSystemRetrievalService;
import at.fhjoanneum.ippr.gateway.security.usermapping.retrieval.memory.UserGroupMemoryCondition;
import at.fhjoanneum.ippr.gateway.security.usermapping.retrieval.memory.UserGroupSystemRetrievalServiceMemoryImpl;

@Configuration
public class UserGroupConfig {

  @Bean(name = "userGroupSystemRetrievalService")
  @Conditional(UserGroupMemoryCondition.class)
  public UserGroupSystemRetrievalService userGroupMemoryService() {
    return new UserGroupSystemRetrievalServiceMemoryImpl();
  }
}
