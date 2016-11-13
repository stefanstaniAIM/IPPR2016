package at.fhjoanneum.ippr.processengine.akka.config;

import org.springframework.stereotype.Component;

import com.typesafe.config.ConfigFactory;

import akka.actor.ActorSystem;

@Component
public class AkkaApplicationContext {

  public ActorSystem getActorSystem() {
    final ActorSystem system = ActorSystem.create("ProcessEngine", ConfigFactory.load());
    return system;
  }
}
