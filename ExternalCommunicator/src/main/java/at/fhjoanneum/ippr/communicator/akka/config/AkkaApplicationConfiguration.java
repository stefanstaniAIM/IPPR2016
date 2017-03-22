package at.fhjoanneum.ippr.communicator.akka.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

@Configuration
public class AkkaApplicationConfiguration {

  @Autowired
  private ApplicationContext applicationContext;

  @Autowired
  private SpringExtension springExtension;

  @Bean
  public ActorSystem actorSystem() {
    final ActorSystem actorSystem = ActorSystem.create("ExternalCommunicator", akkaConfiguration());
    springExtension.initialize(applicationContext);
    return actorSystem;
  }

  @Bean
  public ActorRef composeSupervisorActor() {
    return actorSystem().actorOf(springExtension.props("ComposeSupervisorActor"),
        "ComposeSupervisorActor");
  }

  @Bean
  public ActorRef parseSupervisorActor() {
    return actorSystem().actorOf(springExtension.props("ParseSupervisorActor"),
        "ParseSupervisorActor");
  }

  @Bean
  public Config akkaConfiguration() {
    return ConfigFactory.load();
  }
}
