package at.fhjoanneum.ippr.processengine.akka.config;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import akka.actor.Extension;
import akka.actor.Props;

@Component
public class SpringExtension implements Extension {

  private ApplicationContext applicationContext;

  public void initialize(final ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  public Props props(final String actorBeanName) {
    return Props.create(SpringActorProducer.class, applicationContext, actorBeanName);
  }

  public Props props(final String actorBeanName, final Object... args) {
    return Props.create(SpringActorProducer.class, applicationContext, actorBeanName, args);
  }
}
